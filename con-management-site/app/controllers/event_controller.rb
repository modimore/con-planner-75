class EventController < ApplicationController
  before_action: :require_user

  # Event information for Convetion
  def events; @events = Event.where(convention_name: params[:con_name]); end

  def add; @event = Event.new; end

  def create
    @event = Event.new({ name: params[:event][:name],
                         convention_name: params[:con_name],
                         host_name: params[:event][:host_name],
                         description: params[:event][:description],
                         length: params[:event][:length] })
    if @event.save; redirect_to '/convention/'+params[:con_name]+'/events'
    else; redirect_to '/convention/'+params[:con_name]+'/events'; end
  end

  def edit; @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name]); end

  def edit_details
    @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name])
    @event.name = params[:event][:name]
    @event.host_name = params[:event][:host_name]
    @event.description = params[:event][:description]
    @event.length = params[:event][:length]
    @event.save
    redirect_to '/convention/'+params[:con_name]+'/events'
  end

  def remove
    @event = Event.find_by(convention_name: params[:con_name], name: params[:event_name])
    @event.destroy
    redirect_to '/convention/'+params[:con_name]+'/events'
  end

end
