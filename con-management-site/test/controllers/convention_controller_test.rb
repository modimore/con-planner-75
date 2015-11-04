require 'test_helper'

class ConventionControllerTest < ActionController::TestCase
<<<<<<< HEAD

  test "get convention index" do
=======
  test "should get index" do
>>>>>>> master
    get :index
    assert_response :success
  end

<<<<<<< HEAD
  test "get convention events" do
=======
  test "should get events" do
>>>>>>> master
    get :events
    assert_response :success
  end

<<<<<<< HEAD
  test "get convention schedule" do
=======
  test "should get schedule" do
>>>>>>> master
    get :schedule
    assert_response :success
  end

<<<<<<< HEAD
  test "get convention documents" do
    get :documents
    assert_response :success
  end
  
  test "get convention details" do
=======
  test "should get documents" do
    get :documents
    assert_response :success
  end

  test "should get details" do
>>>>>>> master
    get :details
    assert_response :success
  end

<<<<<<< HEAD
  test "create convention" do
    post( :create_convention, convention: {name: "Example", description: => "Some convention", location: => "Troy, NY"})
    assert_response :success
    assert_redirect_to '/convention/'+params[:convention][:name]+'/index'
  end
=======
>>>>>>> master
end
