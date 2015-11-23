class HomeController < ApplicationController

	# display all conventions in system on home page
	def home; @conventions = Convention.all; end

end
