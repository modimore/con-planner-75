require 'test_helper'

class ConventionControllerTest < ActionController::TestCase
  test "should get index" do
    get :index
    assert_response :success
  end

  test "should get events" do
    get :events
    assert_response :success
  end

  test "should get schedule" do
    get :schedule
    assert_response :success
  end

  test "should get documents" do
    get :documents
    assert_response :success
  end

  test "should get details" do
    get :details
    assert_response :success
  end

end
