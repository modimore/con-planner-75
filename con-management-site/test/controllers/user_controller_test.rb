require 'test_helper'

class UserControllerTest < ActionController::TestCase
    test "signup page" do
        get :new
        assert_response :success
    end

    test "login page" do
        get :login_page
        assert_response :success
    end

    test "signup empty username" do
        assert_no_difference('User.count') do
            get :create, {'username' => '', 'password' => 'password'}
        end
        assert_redirected_to '/signup'
    end

    test "signup empty password" do
        assert_no_difference('User.count') do
            get :create, {'username' => 'test2', 'password' => ''}
        end
        assert_redirected_to '/signup'
    end

    test "signup duplicate username" do
        assert_no_difference('User.count') do
            get :create, {'username' => 'test1', 'password' => 'password'}
        end
        assert_redirected_to '/signup'
    end

    test "signup valid user" do
        assert_difference('User.count') do
            get :create, {'username' => 'test2', 'password' => 'password'}
        end
        assert_redirected_to '/'
    end

    test "login no user match" do
        get :login, {'username' => 'test2', 'password' => 'password'}
        assert_redirected_to '/login'
    end

    test "login wrong password" do
        get :login, {'username' => 'test1', 'password' => 'pa$$word'}
        assert_redirected_to '/login'
    end

    test "login correct password" do
        get :login, {'username' => 'test1', 'password' => 'password'}
        assert_redirected_to '/conventions/mine'
    end
end
