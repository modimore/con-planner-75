class EventController < ApplicationController
  before_action :require_organizer

  # event information for Convetion
  def events; @events = Event.where(convention_name: params[:con_name]); end

  # page for adding new event to convention
  def add
    @event = Event.new
    @hosts = Host.where(convention_name: params[:con_name]).pluck("name")
  end

  # function to update database with event
  def create
    if Event.where(name: params[:event][:name]).length > 0
      redirect_to URI.escape('/convention/'+params[:con_name]+'/events/add')
    elsif params[:event][:length].to_i <= 0
      redirect_to URI.escape('/convention/'+params[:con_name]+'/events/add')
    elsif params[:event][:name] == ''
      redirect_to URI.escape('/convention/'+params[:con_name]+'/events/add')
    else
      @event = Event.new({ name: params[:event][:name],
                           convention_name: params[:con_name],
                           host_name: params[:host_name],
                           description: params[:event][:description],
                           length: params[:event][:length] })
      if @event.save; redirect_to '/convention/'+URI.escape(params[:con_name])+'/events'
      else; redirect_to '/convention/'+URI.escape(params[:con_name])+'/events'; end
    end
  end

  # page for editing event details
  def edit
    # find event details
    @event = Event.find_by( convention_name: params[:con_name],
                            name: params[:event_name])
    # find list of possible hosts for the convention
    @hosts = Host.where( convention_name: params[:con_name] ).pluck("name")
  end

  # update database record for an event
  def update
    if Event.where(name: params[:event][:name]).length > 0 && params[:event][:name] != params[:event_name]
      redirect_to URI.escape('/convention/'+params[:con_name]+'/events/'+params[:event_name]+'/edit')
    elsif params[:event][:length].to_i <= 0
      redirect_to URI.escape('/convention/'+params[:con_name]+'/events/'+params[:event_name]+'/edit')
    elsif params[:event][:name] == ''
      redirect_to URI.escape('/convention/'+params[:con_name]+'/events/'+params[:event_name]+'/edit')
    else
      @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name])
      @event.name = params[:event][:name]
      @event.host_name = params[:host_name]
      @event.description = params[:event][:description]
      @event.length = params[:event][:length]
      @event.save
      redirect_to '/convention/'+URI.escape(params[:con_name])+'/events'
    end
  end

  # delete record of an event
  def delete
    @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name])
    @event.destroy
    redirect_to '/convention/'+URI.escape(params[:con_name])+'/events'
  end

end
