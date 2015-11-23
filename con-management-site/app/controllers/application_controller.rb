class ApplicationController < ActionController::Base
  include ApplicationHelper
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  # when the user is not signed in prompt them to create account
  def require_user
    unless session[:username]; redirect_to '/signup'; end
  end

  # when the user is not an organizer
  # direct them to the conventions for which they are an organizer
  def require_organizer
    unless session[:username] && is_organizer?(params[:con_name])
      redirect_to '/conventions/mine'
    end
  end

end
