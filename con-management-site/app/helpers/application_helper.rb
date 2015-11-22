module ApplicationHelper

  # check if user is logged in
  def logged_in?; session[:username] != nil; end

  # check if user is an organizer for a convention
  def is_organizer?(con_name)
    Organizer.find_by(username: session[:username],
                      convention: con_name) != nil
  end

  # check if user is an administrator for a convention
  def is_administrator?(con_name)
    Organizer.find_by(username: session[:username],
                      convention: con_name,
                      role: "Administrator") != nil
  end

end
