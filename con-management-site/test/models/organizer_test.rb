require 'test_helper'

class OrganizerTest < ActiveSupport::TestCase

  def setup
    @organizer = Organizer.new(name: "Example", email: "organizer@example.com")
  end

  test "should be valid" do
    assert @organizer.valid?
  end

  test "name requirement" do
  	@organizer.name = " "
  	assert_not @organizer.valid?
end