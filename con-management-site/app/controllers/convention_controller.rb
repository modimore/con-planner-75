class ConventionController < ApplicationController
  protect_from_forgery except: [:edit_details,:add_room,:add_host]
  before_action :require_user, except: [:client_search,:download]

  # convention-independent actions =================================
  # page to view all conventions
  def all; @conventions = Convention.all; end

  # search for convention
  def search
    # currently entire search term must be in convention's name
    @conventions = Convention.where("name LIKE ?", "%#{params[:search_term]}%")
  end

  # convention creation page, given new empty convention
  def new; @convention = Convention.new; end

  # add convention to database
  def create_convention
    # if the convention exists already return to list of conventions
    # if not make it and go to its page
    if Convention.where(name: params[:convention][:name]).length > 0
      redirect_to '/convention/all'
    else
      # create an entry in the conventions table
      # and an administrator in the organizers table
      @convention = Convention.new({ name: params[:convention][:name],
                                     description: params[:convention][:description],
                                     location: params[:convention][:location],
                                     start: params[:convention][:start],
                                     end: params[:convention][:end] })
      @organizer = Organizer.new({ username: session[:username],
                                   convention: params[:convention][:name],
                                   role: "Administrator" })
      g_admin = Organizer.new({ username: session[:username],
                                convention: params[:convention][:name],
                                role: "Administrator" })
      if @convention.save && @organizer.save && g_admin.save
        redirect_to '/convention/'+params[:convention][:name]+'/index'
      else; redirect_to '/'; end
    end
  end
  # ================================================================

  # convention-dependent actions ===================================
  # convention's index page
  def index; @convention = Convention.find_by(name: params[:con_name]); end

  # delete convention and everything associated from database
  def delete
    @documents = Document.where(convention_name: params[:con_name])
    @documents.each do |d|
      File.delete(Rails.root.join('public', d.location))
      d.destroy
    end
    Room.where(convention_name: params[:con_name]).each { |r| r.destroy }
    Host.where(convention_name: params[:con_name]).each { |h| h.destroy }
    Event.where(convention_name: params[:con_name]).each { |e| e.destroy }
    Organizer.where(convention: params[:con_name]).each { |o| o.destroy }
    Convention.find_by(name: params[:con_name]).destroy
    redirect_to '/convention/all'
  end
  # ================================================================

  # Convention information =========================================
  def details
    @convention = Convention.find_by(name: params[:con_name])
    @rooms = Room.where(convention_name: params[:con_name])
    @hosts = Host.where(convention_name: params[:con_name])
    @breaks = Break.where(con_name: params[:con_name]).order(:start)
  end

  # open page with forms for edits
  def edit
    @convention = Convention.find_by(name: params[:con_name])
    @rooms = Room.where(convention_name: params[:con_name])
    @room = Room.new
    @hosts = Host.where(convention_name: params[:con_name])
    @host = Host.new
    @breaks = Break.where(con_name: params[:con_name])
    @break = Break.new
  end

  # submit edits to database
  def edit_details
    @convention = Convention.find_by(name: params[:con_name])
    @convention.description = params[:con_descr]
    @convention.location = params[:con_location]
    @convention.start = params[:con_start_time]
    @convention.end = params[:con_end_time]
    if @convention.save; redirect_to '/convention/'+params[:con_name]
    else; redirect_to '/'; end
  end
  # ================================================================

  # Organizers =====================================================
  # view all organizers for a specific convention
  def organizers
    @organizers = Organizer.where(convention: params[:con_name])
  end

  # add organizer to a convention
  def add_organizer
    @organizer = Organizer.new({ username: session[:username], convention: params[:con_name], role: "Volunteer"})
    if @organizer.save; redirect_to '/conventions/mine'
    else; redirect_to '/conventions/search/' + params[:con_name]; end
  end

  # change the role of an organizer for a convention (volunteer, staff, administrator)
  def change_organizer_role
    @organizer = Organizer.find_by(username: params[:username], convention: params[:con_name])
    @organizer.role = params[:new_role]
    @organizer.save
    redirect_to '/convention/' + params[:con_name] + '/organizers'
  end

  # remove an organizer from a convention
  # can be used either on yourself or as an administrator
  def remove_organizer
    username = params[:username] || session[:username]
    @organizer = Organizer.find_by(convention: params[:con_name], username: username)
    @organizer.destroy
    if Organizer.where(convention: params[:con_name]).length <= 0
      delete
    elsif username == session[:username]; redirect_to '/conventions/mine'
    else; redirect_to '/convention/' + params[:con_name] + '/organizers'; end
  end
  # ================================================================

  # Breaks =========================================================
  def add_break
    @break = Break.new({ con_name: params[:con_name],
                         start: params[:break_start_time],
                         end: params[:break_end_time] })
    @break.save
    redirect_to '/convention/' + params[:con_name] + '/edit'
  end

  def remove_break
    Break.find(params[:id]).destroy
    redirect_to '/convention/' + params[:con_name] + '/edit'
  end
  # ================================================================

  # Rooms ==========================================================
  def add_room
    @room = Room.new({ room_name: params[:room_name], convention_name: params[:con_name] })
    if @room.save
      redirect_to '/convention/'+params[:con_name]+'/details' #breaks when I use string interpolation??
    else
      redirect_to '/convention/'+params[:con_name]+'/details' #breaks when I use string interpolation??
    end
  end

  def remove_room
    @room = Room.where( room_name: params[:room_name], convention_name: params[:con_name] )
    @room.each { |r| r.destroy }
    redirect_to '/convention/' + params[:con_name] + '/details'
  end
  # ================================================================

  # Hosts ==========================================================
  def add_host
    @host = Host.new({ name: params[:host_name], convention_name: params[:con_name] })
    if @host.save
      redirect_to '/convention/'+params[:con_name]+'/details' #breaks when I use string interpolation??
    else
      redirect_to '/convention/'+params[:con_name]+'/details' #breaks when I use string interpolation??
    end
  end

  def remove_host
    @host = Host.where( name: params[:host_name], convention_name: params[:con_name] )
    @host.each { |h| h.destroy }
    redirect_to '/convention/' + params[:con_name] + '/details'
  end
  # ================================================================

  # Documents for convention =======================================
  # view all documents for a convention
  def documents
    @documents = Document.where(convention_name: params[:con_name])
    @document = Document.new
  end

  # add a document to a conventio, upload file
  def upload_document
    upload = params[:document] # uploaded file
    # create database entry for file
    @document = Document.new({ display_name: params[:display_name],
                               convention_name: params[:con_name],
                               location: 'uploads/'+upload.original_filename })
    if @document.display_name == ""; @document.display_name = "<no name>"; end
    if @document.save
      # save file on server
      File.open(Rails.root.join('public', 'uploads', upload.original_filename), 'wb') do |file|
        file.write(upload.read)
      end
    end
    redirect_to '/convention/'+params[:con_name]+'/documents'
  end

  # remove document from convention, delete file
  def remove_document
    @document = Document.find_by( convention_name: params[:con_name], display_name: params[:doc_name])
    File.delete(Rails.root.join('public', @document.location))
    @document.destroy
    redirect_to '/convention/'+params[:con_name]+'/documents'
  end
  # ================================================================

  # Schedule =======================================================
  # compute the times a convention is open
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

  def schedule
    #require 'convention_helper'

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
    puts rlist.to_s

    # proceed to scheduling
    scheduler = Scheduler.new(rlist,con_hours)
    puts "Begin scheduling..."
    @schedule = scheduler.run(elist)
  end
  # ================================================================

  # Mobile app stuff ===============================================
  #Sends a lean json that has all of the convention names matching the query string
  def client_search
    @conventions = Convention.where("name LIKE ?" , "%" + params[:query] + "%")

    if @conventions.empty?
      render json: {}
    else
      results = {}
      results[:conventions] = @conventions.as_json
      render json: results
    end
  end

  def download
    @convention = Convention.find_by(name: params[:convention_name])
    @events = Event.where(convention_name: params[:convention_name])
    @rooms = Room.where(convention_name: params[:convention_name])
    @hosts = Host.where(convention_name: params[:convention_name])
    @documents = Document.where(convention_name: params[:convention_name])

    convention_info = {}
    convention_info[:name] = @convention.name
    convention_info[:description] = @convention.description
    convention_info[:location] = @convention.location
    convention_info[:start] = @convention.start
    convention_info[:end] = @convention.end
    convention_info[:events] = @events.as_json
    convention_info[:rooms] = @rooms.as_json
    convention_info[:hosts] = @hosts.as_json
    convention_info[:documents] = @documents.as_json
    render json: convention_info
  end
  # ================================================================

end
