class ScheduleController < ApplicationController

  def times_open(con_name)
    # get convention start time and end time
    con = Convention.select("start","end").find_by(name: con_name)
    # get all the breaks in the convention schedule, sorted by start time
    breaks = Break.where(con_name: con_name).order(:start )

    if breaks.empty? # if there are no breaks return full convention length
      [[0,scheduler_unit_time(con.end-con.start)]]
    else # otherwise
      # the first time block is from the convention start to the beginning of the first break
      times = [ [0,scheduler_unit_time(breaks[0].start-con.start)] ]
      # time blocks in the middle are from a break's start time to the next break's end time
      for i in 0..(breaks.length-2)
        times.append([scheduler_unit_time(breaks[i].end-con.start),
                      scheduler_unit_time(breaks[i+1].start-con.start)])
      end
      # the last time block is from the end of the last break to the convention's end
      times.append([scheduler_unit_time(breaks[breaks.length-1].end-con.start),
                    scheduler_unit_time(con.end-con.start)])
    end
  end

  def new
    #require 'schedule_helper'

    # find correct convention and the hours it is open
    @con = Convention.find_by(name: params[:con_name])
    con_hours = times_open(@con.name)

    # get events for convention from database
    # organize into an array of EventX objects
    elist = []
    Event.where(convention_name: params[:con_name]).each do |e|
      elist.append(EventX.new(e.length,con_hours,[e.host_name],e.name))
    end

    # get rooms for convention from database
    rlist = Room.where(convention_name: params[:con_name]).pluck("room_name")

    # proceed to scheduling
    scheduler = Scheduler.new(rlist,con_hours)
    puts "Begin scheduling..."
    @schedule = scheduler.run(elist)
  end

  def create

    # find convention and the hours that it is open
    con = Convention.find_by(name: params[:con_name])
    con_hours = times_open(con.name)
    versions = Schedule.where(convention: params[:con_name]).pluck("version") || 0
    version = (versions.max { |a, b| a <=> b }) + 1


    # get events for convention from database
    # organize into an array of EventX objects
    elist = []
    Event.where(convention_name: params[:con_name]).each do |e|
      elist.append(EventX.new(e.length,con_hours,[e.host_name],e.name))
    end

    # get rooms for convention from database
    rlist = Room.where(convention_name: params[:con_name]).pluck("room_name")

    # proceed to scheduling
    scheduler = Scheduler.new(rlist,con_hours)
    puts "Begin scheduling..."
    schedule = scheduler.run(elist)

    schedule.keys.each do |r|
      schedule[r].each do |e|
        Schedule.new({ convention: params[:con_name],
                       version: version,
                       event: e.name,
                       start: con.start + e.times[0][0]*3600,
                       end: con.start + e.times[0][1]*3600,
                       room: r }).save
      end
    end
    redirect_to '/convention/' + params[:con_name]
  end

  def view
    # select proper version of schedule for specificed convention
    con_schedule = Schedule.where(convention: params[:con_name])
    version = params[:schedule_version] || (con_schedule.pluck("version").max { |a, b| a <=> b })
    con_schedule = con_schedule.where(version: version).order(:start)

    @schedule = Hash.new
    con_schedule.pluck("room").each do |r|
      @schedule[r] = []
    end

    con_schedule.each do |e|
      @schedule[e.room].append(EventY.new(e.event,e.start,e.end))
    end
  end

  def update
  end

  def delete
  end
end
