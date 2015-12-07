require 'test_helper'

class ConventionControllerTest < ActionController::TestCase

    def setup
        
    end

    test "convention create page" do
        get :new, {}, {'username' => 'test1'}
        assert_response :success
    end

    test "convention create empty name" do
        @convention = {'name' => '', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => '2015-01-01T17:00:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'} 
        end
        assert_redirected_to '/convention/new'
    end

    test "convention create empty start" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '', 'end' => '2015-01-01T05:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "convention create empty end" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => ''}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "convention create duplicate name" do
        @convention = {'name' => 'Test Convention 1', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => '2015-01-01T17:00:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "convention create invalid times" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T17:00', 'end' => '2015-01-01T09:00:00'}
        assert_no_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/new'
    end

    test "convention create valid convention" do
        @convention = {'name' => 'Test Convention 2', 'description' => 'a convention', 'location' => 'a place', 'start' => '2015-01-01T09:00', 'end' => '2015-01-01T17:00:00'}
        assert_difference('Convention.count') do
            get :create, {'convention' => @convention}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/Test%20Convention%202/index'
    end

    test "convention edit page" do
        get :edit, {'con_name' => 'Test Convention 1'}, {'username' => 'test1'}
        assert_response :success
    end

    test "convention edit empty start" do
        @convention = {'con_name' => 'Test Convention 1', 'con_descr' => 'asdfg', 'con_location' => 'here', 'con_start_time' => '', 'con_end_time' => '2015-01-01T17:00:00'}
        get :update, @convention, {'username' => 'test1'}
        @result = Convention.find_by(name: 'Test Convention 1')
        assert_not_equal(nil, @result.start,  'Able to set empty start time')
        assert_equal('2015-01-01 09:00:00', @result.start.to_s(:db), 'Setting an empty start time does not preserve the previous')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "convention edit empty end" do
        @convention = {'con_name' => 'Test Convention 1', 'con_descr' => 'asdfg', 'con_location' => 'here', 'con_start_time' => '2015-01-01T09:00:00', 'con_end_time' => ''}
        get :update, @convention, {'username' => 'test1'}
        @result = Convention.find_by(name: 'Test Convention 1')
        assert_not_equal(nil, @result.end,  'Able to set empty end time')
        assert_equal('2015-01-01 17:00:00', @result.end.to_s(:db), 'Setting an empty end time does not preserve the previous')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "convention edit invalid times" do
        @convention = {'con_name' => 'Test Convention 1', 'con_descr' => 'asdfg', 'con_location' => 'here', 'con_start_time' => '2015-01-01T17:00:00', 'con_end_time' => '2015-01-01T09:00:00'}
        get :update, @convention, {'username' => 'test1'}
        @result = Convention.find_by(name: 'Test Convention 1')
        assert_equal('2015-01-01 17:00:00', @result.end.to_s(:db), 'Able to set invalid times')
        assert_equal('2015-01-01 09:00:00', @result.start.to_s(:db), 'Able to set invalid times')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "convention edit valid edit" do
        @convention = {'con_name' => 'Test Convention 1', 'con_descr' => 'asdfg', 'con_location' => 'here', 'con_start_time' => '2015-01-01T10:00:00', 'con_end_time' => '2015-01-01T19:00:00'}
        get :update, @convention, {'username' => 'test1'}
        @result = Convention.find_by(name: 'Test Convention 1')
        assert_equal('asdfg', @result.description, 'Unable to edit description')
        assert_equal('here', @result.location, 'Unable to edit location')
        assert_equal('2015-01-01 10:00:00', @result.start.to_s(:db), 'Unable to edit start time')
        assert_equal('2015-01-01 19:00:00', @result.end.to_s(:db), 'Unable to edit end time')
        assert_redirected_to '/convention/Test%20Convention%201'
    end

    test "host empty name" do 
        assert_no_difference('Host.count') do
            get :add_host, {'con_name' => 'Test Convention 1', 'host_name' => ''}, {'username' => 'test1'}
        end
        assert_equal(nil, Host.find_by(name: ''), 'Added empty host')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "host duplicate name" do
        assert_no_difference('Host.count') do
            get :add_host, {'con_name' => 'Test Convention 1', 'host_name' => 'Host 1'}, {'username' => 'test1'}
        end
        assert_equal(2, Host.where(name: 'Host 1'))
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "host add" do
        assert_difference('Host.count') do
            get :add_host, {'con_name' => 'Test Convention 1', 'host_name' => 'Host 2'}, {'username' => 'test1'}
        end
        assert_not_equal(nil, Host.find_by(name: 'Host 2'), 'Failed to add host')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "host remove" do
        assert_difference('Host.count', -1) do
            get :remove_host, {'con_name' => 'Test Convention 1', 'host_name' => 'Host 1'}, {'username' => 'test1'}
        end
        assert_equal(nil, Host.find_by(name: 'Host 1'), 'Failed to remove host')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "room empty name" do 
        assert_no_difference('Room.count') do
            get :add_room, {'con_name' => 'Test Convention 1', 'room_name' => ''}, {'username' => 'test1'}
        end
        assert_equal(nil, Room.find_by(room_name: ''), 'Added empty room')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "room duplicate name" do
        assert_no_difference('Room.count') do
            get :add_room, {'con_name' => 'Test Convention 1', 'room_name' => 'Room 1'}, {'username' => 'test1'}
        end
        assert_equal(2, Room.where(room_name: 'Room 1'))
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "room add" do
        assert_difference('Room.count') do
            get :add_room, {'con_name' => 'Test Convention 1', 'room_name' => 'Room 2'}, {'username' => 'test1'}
        end
        assert_not_equal(nil, Room.find_by(room_name: 'Room 2'), 'Failed to add room')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "room remove" do
        assert_difference('Room.count', -1) do
            get :remove_room, {'con_name' => 'Test Convention 1', 'room_name' => 'Room 1'}, {'username' => 'test1'}
        end
        assert_equal(nil, Room.find_by(room_name: 'Room 1'), 'Failed to remove room')
        assert_redirected_to '/convention/Test%20Convention%201/edit'
    end

    test "documents page" do
        get :documents, {'con_name' => 'Test Convention 1'}, {'username' => 'test1'}
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

 