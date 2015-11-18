class UserController < ApplicationController

  def new; @user = User.new; end

  def create

    if User.where(username: params[:username]).length > 0
      redirect_to 'home/login'
    else
      salt = BCrypt::Engine.generate_salt
      pw_hash = BCrypt::Engine.hash_secret(params[:password], salt)
      @user = User.new(username: params[:username], password_digest: pw_hash, salt: salt)
      if @user.save
        session[:username] = @user.username
        redirect_to '/home/index'
      else; redirect_to '/home/index'; end
    end
  end

  def login; end

  def login_x
    @user = User.find_by(username: params[:username])
    if @user && @user.authenticate(params[:password])
      session[:username] = @user.username
      redirect_to '/home/index'
    else; redirect_to '/login'; end
  end

end
