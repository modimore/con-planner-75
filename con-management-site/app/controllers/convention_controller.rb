class ConventionController < ApplicationController
  protect_from_forgery except: [:edit_details,:add_room,:add_host]
  before_action :require_user, except: [:client_search,:download]

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

  # convention's index page, send specific convention details
  def index; @convention = Convention.find_by(name: params[:con_name]); end

  # convention table editing =======================================
  # edit convention information page
  def edit
    @convention = Convention.find_by(name: params[:con_name])
    @rooms = Room.where(convention_name: params[:con_name])
    @room = Room.new
    @hosts = Host.where(convention_name: params[:con_name])
    @host = Host.new
  end

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

  # Convention information pages ===================================
  def details
    @convention = Convention.find_by(name: params[:con_name])
    @rooms = Room.where(convention_name: params[:con_name])
    @new_room = Room.new
    @hosts = Host.where(convention_name: params[:con_name])
    @new_host = Host.new
  end
  # ================================================================

  # Organizers =====================================================
  def organizers
    @organizers = Organizer.where(convention: params[:con_name])
  end

  def add_organizer
    @organizer = Organizer.new({ username: session[:username], convention: params[:con_name], role: "Volunteer"})
    if @organizer.save; redirect_to '/conventions/mine'
    else; redirect_to '/conventions/search/' + params[:con_name]; end
  end

  def change_organizer_role
    @organizer = Organizer.find_by(username: params[:username], convention: params[:con_name])
    @organizer.role = params[:new_role]
    @organizer.save
    redirect_to '/convention/' + params[:con_name] + '/organizers'
  end

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
