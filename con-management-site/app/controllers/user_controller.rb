class UserController < ApplicationController

  # new user page
  def new; @user = User.new; end

  # add user to the database
  def create
    if User.where(username: params[:username]).length > 0
      redirect_to '/signup'
    else
      salt = BCrypt::Engine.generate_salt
      pw_hash = BCrypt::Engine.hash_secret(params[:password], salt)
      @user = User.new(username: params[:username], password_digest: pw_hash, salt: salt)
      if @user.save; session[:username] = @user.username; end
      redirect_to '/'
    end
  end

  # login page
  def login_page; end

  # authenticate user, add info to session
  def login
    @user = User.find_by(username: params[:username])
    if @user && @user.authenticate(params[:password])
      session[:username] = @user.username
      redirect_to '/conventions/mine'
    else; redirect_to '/login'; end
  end

  # remove username from session
  def logout
    session.delete(:username)
    redirect_to '/'
  end

  # page to view all conventions for a specific user
  def conventions
    # get array of names of all conventions where user is an organizer
    convention_names = Organizer.where(username: session[:username]).select("convention")
    # get all conventions whose name is in our array of convention names
    @conventions = Convention.where(name: convention_names)
  end

end
