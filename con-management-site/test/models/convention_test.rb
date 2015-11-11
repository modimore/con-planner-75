require 'test_helper'

class ConventionTest < ActiveSupport::TestCase

  def setup
    @no_name = Convention.new(name: " ", description: "con", location: "here")
  end

  test "ensure convention is valid" do
    assert conventions(:base).valid?
  end

  test "name requirement" do
  	assert_not @no_name.valid?
  end
end

