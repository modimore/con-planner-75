class EventController < ApplicationController
  before_action :require_organizer

  # Event information for Convetion
  def events; @events = Event.where(convention_name: params[:con_name]); end

  # page for adding new event to convention
  def add
    @event = Event.new
    @hosts = Host.where(convention_name: params[:con_name]).pluck("name")
  end

  # function to update database with event
  def create
    @event = Event.new({ name: params[:event][:name],
                         convention_name: params[:con_name],
                         host_name: params[:host_name],
                         description: params[:event][:description],
                         length: params[:event][:length] })
    if @event.save; redirect_to '/convention/'+params[:con_name]+'/events'
    else; redirect_to '/convention/'+params[:con_name]+'/events'; end
  end

  def edit
    @event = Event.find_by( convention_name: params[:con_name],
                            name: params[:event_name])
    @hosts = Host.where( convention_name: params[:con_name] ).pluck("name")
  end

  def update
    @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name])
    @event.name = params[:event][:name]
    @event.host_name = params[:host_name]
    @event.description = params[:event][:description]
    @event.length = params[:event][:length]
    @event.save
    redirect_to '/convention/'+params[:con_name]+'/events'
  end

  def delete
    @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name])
    @event.destroy
    redirect_to '/convention/'+params[:con_name]+'/events'
  end

end
