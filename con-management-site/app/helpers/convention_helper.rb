module ConventionHelper
end

# mini event class (not connected to db) for testing purposes
class EventX
  attr_reader :length, :hosts, :name
  attr_accessor :times

  # vars: time length of event (hrs);
  # list of acceptable times to schedule event in the format [[start1, end1], [start2, end2], etc.],
    # where the start and end can encompass any length of time within the overall convention time;
  # list of hosts of event;
  # name of event (must be unique)
  def initialize(length, times, hosts, name)
    @length = length
    @times = times
    @hosts = hosts
    @name = name
  end

end

# Scheduler class
# Used to generate schedules for the convention
class Scheduler
  attr_reader :events, :roomhash, :rooms

  # vars: list of rooms available; time brackets during which convention runs
  def initialize(rooms, times)
    @rooms = rooms
    @times = times

    # create 1 key per each room in hash
    @roomhash = Hash.new
    @rooms.each do |r|
      @roomhash[r] = Array.new
    end

    # initialize to all rooms available at all times
    @availability = Hash.new
    @times.each do |t|
      for i in t[0]..t[1]
        @availability[i] = @rooms.dup
      end
    end
  end

  # publicly accessed runner for scheduler; calls backtracking algorithm & changes @events to solution
  def run(eventlist)
    solution = backtrack(eventlist)
    unless solution=="fail"
      @events = solution
      @roomhash.each do |k,v|
        v.sort! do |event1, event2|
          event1.times[0][0] <=> event2.times[0][0]
        end
      end

      @roomhash
    else
      "failure"
    end
  end


  # private methods
  private

  # removes all time brackets that are smaller than event length
  def remove_times(event)
    event.times.each do |t|
      if t[1].to_i-t[0].to_i < event.length
        event.times.delete(t)
      end
    end
  end

  # determines total slack available (in hrs) in picking a time for the event
  def get_slack(event)
    remove_times(event)
    total = 0
    event.times.each do |t|
      total += (t[1].to_i - t[0].to_i)
    end
    total-event.length
  end

  # removes times in event that conflict with start & end times v1 & v2
  def trim_times(event,v1,v2)
    event.times.each do |t|
      if t[0]==v1 && t[1]==v2
        event.times.delete(t)
      elsif t[0] >= v1 && t[0] < v2
        t[0] = v2
      elsif t[1] <= v2 && t[1] > v1
        t[1] = v1
      elsif t[0] < v1 && t[1] > v2
        temp = t[1]
        t[1] = v1
        event.times.push([v2,temp])
      end
    end
  end

  # backtracking algorithm to find a successful schedule assignment
  def backtrack(input_elist)

    elist = Marshal.load(Marshal.dump(input_elist))

    # sort events based on slack
    elist.sort! do |event1, event2|
      get_slack(event1) <=> get_slack(event2)
    end

    # find next event to assign (the one with the least slack) and remove from event list
    curr_event = elist.delete_at(0)

    # make array of all possible start times for current event
    start_vals = []
    curr_event.times.each do |t|
      for i in t[0]..(t[1]-curr_event.length)
        start_vals.push(i)
      end
    end
    puts "starting #{curr_event.name}, all times: #{curr_event.times}"
    # try each assigning each possible value until one works
    start_vals.each do |v1|
      v2 = v1+curr_event.length
      curr_event.times=([[v1,v2]])

      # uses 1 room for the chosen time value
      # make an array containing every room that is available for the entire chosen time bracket
      intersect = @availability[v1]
      for i in (v1+1)...v2
        intersect = intersect & @availability[i]
      end
      puts "looking for room for #{curr_event.name} from #{v1} to #{v2}. Available: #{intersect}"
      #puts "available times for next event: #{elist[0].times}"
      # assign curr_event to the first room in the array of available rooms
      unless intersect.empty?
        # variable to store name of room; used later if chosen time value fails
        curr_room = intersect[0]

        @roomhash[curr_room].push(curr_event)

        # this room becomes unavailable for every hour in the chosen time bracket
        for i in v1...v2
          @availability[i].delete(curr_room)
        end

        puts "trying #{curr_event.name} from #{v1} to #{v2} in #{curr_room}"
      else
        # no available room; skip the rest of this iteration of the loop
        next
      end

      # base case - have assigned values to every event from original list
      if elist.empty?
        return [curr_event]
      end

      # store current state of events list, to revert back to later if this path fails
      temp_elist = Marshal.load(Marshal.dump(elist))

      # check for host conflicts - a host can't be at 2 events at the same time
      elist.each do |e|
        curr_event.hosts.each do |h|
          # for each event, if it shares a host with curr_event
          if e.hosts.include?(h)
            trim_times(e,v1,v2)
          end
        end
      end

      # re-sort & check slack; if any events have a negative amount of slack, this time assignment doesn't work
      elist.sort! do |event1, event2|
        get_slack(event1) <=> get_slack(event2)
      end

      if get_slack(elist[0]) >= 0

        # this time assignment works; recursively call backtrack algorithm to assign a time to the next event in the list
        result = backtrack(elist)
        return result + [curr_event] unless result=="fail"
      end

      # didn't return success in if statements; remove v from potential start times,
      # remove curr_event from room hash, reset room availability, un-trim altered times
      # for i in v1..v2
      # 	@availability[i] += 1
      # end
      for i in v1...v2
        @availability[i].push(curr_room)
      end
      roomhash[curr_room].delete(curr_event)
      elist = Marshal.load(Marshal.dump(temp_elist))
    end

    # went through all recursive calls without returning a success
    puts "returning fail on #{curr_event.name}"
    return "fail"
  end
end
