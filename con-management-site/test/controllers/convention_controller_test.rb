require 'test_helper'

class ConventionControllerTest < ActionController::TestCase

  def setup
    @convention = Convention.new(name: "Example", description: "An example convention", location: "Troy, NY")
    
  end

  test "get convention" do
    get :index, convention_name: conventions(:base).name
    assert_response :success
  end

  test "create convention" do
    assert_difference('Convention.count') do
      post :create_convention, convention: {name: @convention.name, description: @convention.description, location: @convention.location}
    end
    assert_response :redirect
    assert_redirected_to '/convention/'+@convention.name+'/index'
  end

test "delete convention" do
    assert_differences([['Convention.count', -1],['Document.count', -1]]) do
      patch :delete, convention_name: conventions(:base).name
    end
    assert_response :redirect
    assert_redirected_to '/convention/all'
  end
end

