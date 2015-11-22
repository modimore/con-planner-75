class ApplicationController < ActionController::Base
  include ApplicationHelper
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  # action to take when user is not logged in
  def require_user
    unless session[:username]; redirect_to '/signup'; end
  end

  def require_organizer
    unless session[:username] && is_organizer?(params[:con_name])
      redirect_to '/conventions/mine'
    end
  end

end
