require 'test_helper'

class EventControllerTest < ActionController::TestCase

    test "event create page" do
        get :add, {'con_name' => 'Test Convention 1'}, {'username' => 'test1'}
        assert_response :success
    end

    test "event create empty name" do
        @event = {'name' => '', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 2}
        assert_no_difference('Event.count') do
            get :create, {'con_name' => 'Test Convention 1', 'event' => @event}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/Test%20Convention%201/events/add'
    end

    test "event create invalid length" do
        @event = {'name' => 'Event 2', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 0}
        assert_no_difference('Event.count') do
            get :create, {'con_name' => 'Test Convention 1', 'event' => @event}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/Test%20Convention%201/events/add'
    end

    test "event create duplicate name" do
        @event = {'name' => 'Event 1', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 2}
        assert_no_difference('Event.count') do
            get :create, {'con_name' => 'Test Convention 1', 'event' => @event}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/Test%20Convention%201/events/add'
    end

    test "event create valid event" do
        @event = {'name' => 'Event 2', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 2}
        assert_difference('Event.count') do
            get :create, {'con_name' => 'Test Convention 1', 'event' => @event}, {'username' => 'test1'}
        end
        assert_redirected_to '/convention/Test%20Convention%201/events'
    end

    test "event edit page" do
        get :edit, {'con_name' => 'Test Convention 1', 'event_name' => 'Event 1'}, {'username' => 'test1'}
        assert_response :success
    end

    test "event edit empty name" do
        @event = {'name' => '', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 2}
        get :update, {'con_name' => 'Test Convention 1', 'event_name' => 'Event 1', 'event' => @event}, {'username' => 'test1'}
        assert_equal(nil, Event.find_by(name: ''), 'Stored empty name')
        assert_not_equal(nil, Event.find_by(name: 'Event 1'), 'Does not preserve original event')
        assert_redirected_to '/convention/Test%20Convention%201/events/Event%201/edit'
    end

    test "event edit invalid length" do
        @event = {'name' => 'Event 1', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 0}
        get :update, {'con_name' => 'Test Convention 1', 'event_name' => 'Event 1', 'event' => @event}, {'username' => 'test1'}
        assert_not_equal(0, Event.find_by(name: 'Event 1').length, 'Stored invalid length')
        assert_not_equal(nil, Event.find_by(name: 'Event 1'), 'Does not preserve original event')
        assert_redirected_to '/convention/Test%20Convention%201/events/Event%201/edit'
    end

    test "event edit duplicate name" do
        @event = {'name' => 'Event 3', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 2}
        get :update, {'con_name' => 'Test Convention 1', 'event_name' => 'Event 1', 'event' => @event}, {'username' => 'test1'}
        assert_not_equal(nil, Event.find_by(name: 'Event 1'), 'Does not preserve original event')
        assert_redirected_to '/convention/Test%20Convention%201/events/Event%201/edit'
    end

    test "event edit valid event" do
        @event = {'name' => 'Event 2', 'host_name' => 'Host 1', 'description' => 'an event', 'length' => 2}
        get :update, {'con_name' => 'Test Convention 1', 'event_name' => 'Event 1', 'event' => @event}, {'username' => 'test1'}
        assert_not_equal(nil, Event.find_by(name: 'Event 2'), 'Did not store edit')
        assert_equal(nil, Event.find_by(name: 'Event 1'), 'Did not remove original')
        assert_redirected_to '/convention/Test%20Convention%201/events'
    end

    test "remove event" do
        assert_difference('Event.count', -1) do
            get :delete, {'con_name' => 'Test Convention 1', 'event_name' => 'Event 3'}, {'username' => 'test1'}
        end
        assert_equal(nil, Event.find_by(name: 'Event 3'), 'Did not remove event')
        assert_redirected_to '/convention/Test%20Convention%201/events'
    end

end
