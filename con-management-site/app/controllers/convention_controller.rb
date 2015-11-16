class ConventionController < ApplicationController
  protect_from_forgery except: :details

  def all
    @conventions = Convention.all
  end

  # Convention information
  def new
    @convention = Convention.new
  end

  #Sends a lean json that has all of the conveniton names matching the query string
  def search
    @conventions = Convention.where("name LIKE ?" , "%" + params[:query] + "%")

    if @conventions.empty?
      render json: {}
    else
      results = {}
      results[:conventions] = @conventions.as_json
      render json: results
    end
  end

  def create_convention
    if Convention.where(name: params[:convention][:name]).length > 0
      redirect_to '/convention/all'
    else
      @convention = Convention.new({ name: params[:convention][:name],
                                     description: params[:convention][:description],
                                     location: params[:convention][:location] })
      if @convention.save
        redirect_to '/convention/'+params[:convention][:name]+'/index'
      else
        redirect_to '/'
      end
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

  def delete
    @documents = Document.where(convention_name: params[:convention_name])
    @documents.each do |d|
      File.delete(Rails.root.join('public', d.location))
      d.destroy
    end
    Room.where(convention_name: params[:convention_name]).each { |r| r.destroy }
    Host.where(convention_name: params[:convention_name]).each { |h| h.destroy }
    Event.where(convention_name: params[:convention_name]).each { |e| e.destroy }
    Convention.find_by(name: params[:convention_name]).destroy
    redirect_to '/convention/all'
  end

  def index
    @convention = Convention.find_by(name: params[:convention_name])
  end

  def edit
    @convention = Convention.find_by(name: params[:convention_name])
  end

  # Convention details
  def details
    @convention = Convention.find_by(name: params[:convention_name])
    @rooms = Room.where(convention_name: params[:convention_name])
    @new_room = Room.new
    @hosts = Host.where(convention_name: params[:convention_name])
    @new_host = Host.new
  end

  def edit_details
    @convention = Convention.find_by(name: params[:convention_name])
    @convention.description = params[:con_descr]
    @convention.location = params[:con_location]
    @convention.start = params[:con_start_time]
    @convention.end = params[:con_end_time]
    @convention.save
    redirect_to '/convention/'+params[:convention_name]+'/details'
  end

  def add_room
    @room = Room.new({ room_name: params[:room_name], convention_name: params[:convention_name] })
    if @room.save
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    else
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    end
  end

  def remove_room
    @room = Room.where( room_name: params[:room_name], convention_name: params[:convention_name] )
    @room.each { |r| r.destroy }
    redirect_to '/convention/' + params[:convention_name] + '/details'
  end

  def add_host
    @host = Host.new({ name: params[:host_name], convention_name: params[:convention_name] })
    if @host.save
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    else
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    end
  end

  def remove_host
    @host = Host.where( name: params[:host_name], convention_name: params[:convention_name] )
    @host.each { |h| h.destroy }
    redirect_to '/convention/' + params[:convention_name] + '/details'
  end

  def schedule
  end

  # Documents for convention
  def documents
    @documents = Document.where(convention_name: params[:convention_name])
    @document = Document.new
  end

  def upload_document
    uploaded_io = params[:document]
    @document = Document.new({ display_name: params[:display_name],
                               convention_name: params[:convention_name],
                               location: 'uploads/'+uploaded_io.original_filename })
    if @document.display_name == ""; @document.display_name = "<no name>"; end
    if @document.save
      File.open(Rails.root.join('public', 'uploads', uploaded_io.original_filename), 'wb') do |file|
        file.write(uploaded_io.read)
      end
    end
    redirect_to '/convention/'+params[:convention_name]+'/documents'
  end

  def remove_document
    @document = Document.find_by( convention_name: params[:convention_name], display_name: params[:doc_name])
    File.delete(Rails.root.join('public', @document.location))
    @document.destroy
    redirect_to '/convention/'+params[:convention_name]+'/documents'
  end

end
