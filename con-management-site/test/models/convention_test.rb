require 'test_helper'

class ConventionTest < ActiveSupport::TestCase
<<<<<<< HEAD

  def setup
    @convention = ConventionController.new(name: "Example", description: "An example convention", location: "Troy, NY")
  end

  test "ensure convention is valid" do
    assert @convention.valid?
  end

  test "name requirement" do
  	@convention.name = " "
  	assert_not @convention.valid?
  end
end
=======
  # test "the truth" do
  #   assert true
  # end
end
>>>>>>> master
