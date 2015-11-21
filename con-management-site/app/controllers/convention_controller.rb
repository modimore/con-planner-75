class ConventionController < ApplicationController
  protect_from_forgery except: :details
  before_action :require_user

  # Get all conventions
  def all; @conventions = Convention.all; end

  def by_user
    convention_names = Organizer.where(username: session[:username]).select("convention")
    puts convention_names
    @conventions = Convention.where(name: convention_names)
  end

  def search
    @conventions = Convention.where("name LIKE ?", "%#{params[:search_term]}%")
  end

  # Convention creation page, give new empty convention
  def new; @convention = Convention.new; end

  # Add convention to database
  def create_convention
    # If the convention exists already return to list of conventions
    # If not make it and go to its page
    if Convention.where(name: params[:convention][:name]).length > 0
      redirect_to '/convention/all'
    else
      @convention = Convention.new({ name: params[:convention][:name],
                                     description: params[:convention][:description],
                                     location: params[:convention][:location] })
      if @convention.save; redirect_to '/convention/'+params[:convention][:name]+'/index'
      else; redirect_to '/'; end
    end
  end

  # Delete convention and everything associated from database
  def delete
    @documents = Document.where(convention_name: params[:con_name])
    @documents.each do |d|
      File.delete(Rails.root.join('public', d.location))
      d.destroy
    end
    Room.where(convention_name: params[:con_name]).each { |r| r.destroy }
    Host.where(convention_name: params[:con_name]).each { |h| h.destroy }
    Event.where(convention_name: params[:con_name]).each { |e| e.destroy }
    Convention.find_by(name: params[:con_name]).destroy
    redirect_to '/convention/all'
  end

  # Convention's index page, send specific convention details
  def index; @convention = Convention.find_by(name: params[:con_name]); end

  # Edit convention information page
  def edit; @convention = Convention.find_by(name: params[:con_name]); end

  # Convention details =============================================
  def details
    @convention = Convention.find_by(name: params[:con_name])
    @rooms = Room.where(convention_name: params[:con_name])
    @new_room = Room.new
    @hosts = Host.where(convention_name: params[:con_name])
    @new_host = Host.new
  end

  def edit_details
    @convention = Convention.find_by(name: params[:con_name])
    @convention.description = params[:con_descr]
    @convention.location = params[:con_location]
    @convention.start = params[:con_start_time]
    @convention.end = params[:con_end_time]
    @convention.save
    redirect_to '/convention/'+params[:con_name]+'/details'
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

  # Organizers =====================================================
  def add_organizer
    @organizer = Organizer.new({ username: session[:username], convention: params[:con_name]})
    if @organizer.save; redirect_to '/conventions'
    else; redirect_to '/conventions/search/' + params[:con_name]; end
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

  # Schedule =======================================================
  def schedule
    require 'convention_helper'

    @con = Convention.find_by(name: params[:con_name])

    # get events for convention from database
    elist = []
    Event.where(convention_name: params[:con_name]).each do |e|
      elist.append(EventX.new(e.length,[[0,48]],[e.host_name],e.name))
    end

    # get rooms for convention from database
    rlist = []
    Room.where(convention_name: params[:con_name]).each do |r|
      rlist.append(r.room_name)
    end

    # proceed to scheduling
    contime = [[0,(@con.end - @con.start).to_i/3600]]
    scheduler = Scheduler.new(rlist,contime)
    @schedule = scheduler.run(elist)
  end
  # ================================================================

  # Documents for convention =======================================
  def documents
    @documents = Document.where(convention_name: params[:con_name])
    @document = Document.new
  end

  def upload_document
    uploaded_io = params[:document]
    @document = Document.new({ display_name: params[:display_name],
                               convention_name: params[:con_name],
                               location: 'uploads/'+uploaded_io.original_filename })
    if @document.display_name == ""; @document.display_name = "<no name>"; end
    if @document.save
      File.open(Rails.root.join('public', 'uploads', uploaded_io.original_filename), 'wb') do |file|
        file.write(uploaded_io.read)
      end
    end
    redirect_to '/convention/'+params[:con_name]+'/documents'
  end

  def remove_document
    @document = Document.find_by( convention_name: params[:con_name], display_name: params[:doc_name])
    File.delete(Rails.root.join('public', @document.location))
    @document.destroy
    redirect_to '/convention/'+params[:con_name]+'/documents'
  end
  # ================================================================

  # Mobile app stuff ===============================================
  #Sends a lean json that has all of the conveniton names matching the query string
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
  # Mobile app stuff ===============================================

end
