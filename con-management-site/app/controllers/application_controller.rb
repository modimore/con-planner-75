class ApplicationController < ActionController::Base
  # Prevent CSRF attacks by raising an exception.
  # For APIs, you may want to use :null_session instead.
  protect_from_forgery with: :exception

  # action to take when user is not logged in
  def require_user
    unless session[:username]; redirect_to '/signup'; end
  end

end
