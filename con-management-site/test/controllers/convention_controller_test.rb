require 'test_helper'

class ConventionControllerTest < ActionController::TestCase

  test "get convention" do
    get :index
    assert_response :success
  end
end
