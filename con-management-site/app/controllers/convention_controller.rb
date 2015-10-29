class ConventionController < ApplicationController
  protect_from_forgery except: :details

  def new
    @convention = Convention.new
  end

  def create_convention
    @convention = Convention.new({ name: params[:convention][:name],
                                   description: params[:convention][:description],
                                   location: params[:convention][:location] })
    if @convention.save
      redirect_to '/convention/'+params[:convention][:name]+'/index'
    else
      redirect_to '/'
    end
  end

  def index
    @convention = Convention.find_by(name: params[:name])
  end

  # Event information for Convetion
  def events
    @events = Event.where(convention_name: params[:name])
  end

  def add_event
    @event = Event.new
  end

  def create_event
    @event = Event.new({ name: params[:event][:name],
                         convention_name: params[:convention_name],
                         host_name: params[:event][:host_name],
                         description: params[:event][:description] })
    if @event.save
      redirect_to '/convention/'+params[:convention_name]+'/events'
    else
      redirect_to '/convention/'+params[:convention_name]+'/events'
    end
  end

  def schedule
  end

  def documents
  end

  # Convention details
  def details
    @rooms = Room.where(convention_name: params[:name])
    @new_room = Room.new
    @hosts = Host.where(convention_name: params[:name])
    @new_host = Host.new
  end

  def add_room
    @room = Room.new({ room_name: params[:room_name], convention_name: params[:convention_name] })
    if @room.save
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    else
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    end
  end

  def add_host
    @host = Host.new({ name: params[:host_name], convention_name: params[:convention_name] })
    if @host.save
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    else
      redirect_to '/convention/'+params[:convention_name]+'/details' #breaks when I use string interpolation??
    end
  end

end
