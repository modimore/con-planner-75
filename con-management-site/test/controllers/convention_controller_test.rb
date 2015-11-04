require 'test_helper'

class ConventionControllerTest < ActionController::TestCase

  test "get convention index" do
    get :index
    assert_response :success
  end

  test "get convention events" do
    get :events
    assert_response :success
  end

  test "get convention schedule" do
    get :schedule
    assert_response :success
  end

  test "get convention documents" do
    get :documents
    assert_response :success
  end
  
  test "get convention details" do
    get :details
    assert_response :success
  end

  test "create convention" do
    post( :create_convention, convention: {name: "Example", description: => "Some convention", location: => "Troy, NY"})
    assert_response :success
    assert_redirect_to '/convention/'+params[:convention][:name]+'/index'
  end
end
