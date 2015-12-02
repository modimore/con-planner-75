require 'test_helper'

class ConventionControllerTest < ActionController::TestCase

    def setup
        
    end

    test "create convention page" do
        get :new, {}, {'username' => 'test1'}
        assert_response :success
    end

    test "create convention empty name" do
        @convention = {'name' => '', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => '2015-01-01T17:00:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'} 
        end
        assert_redirected_to '/convention/new'
    end

    test "create convention empty start" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '', 'end' => '2015-01-01T05:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "create convention empty end" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => ''}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "create convention duplicate name" do
        @convention = {'name' => 'Test Convention 1', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => '2015-01-01T17:00:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "create convention invalid times" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T17:00', 'end' => '2015-01-01T09:00:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "create convention valid convention" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T17:00', 'end' => '2015-01-01T09:00:00'}
        assert_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/Test%20Convention%202/index'
    end

    test "edit convention page" do
        get :new, {}, {'username' => 'test1'}
        assert_response :success
    end


#   test "get convention" do
#     get :index, convention_name: conventions(:base).name
#     assert_response :success
#   end

#   test "create convention" do
#     assert_difference('Convention.count') do
#       post :create_convention, convention: {name: @convention.name, description: @convention.description, location: @convention.location}
#     end
#     assert_response :redirect
#     assert_redirected_to '/convention/'+@convention.name+'/index'
#   end

# test "delete convention" do
#     assert_differences([['Convention.count', -1],['Document.count', -1]]) do
#       patch :delete, convention_name: conventions(:base).name
#     end
#     assert_response :redirect
#     assert_redirected_to '/convention/all'
#   end
end

