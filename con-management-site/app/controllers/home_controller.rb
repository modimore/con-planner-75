class HomeController < ApplicationController

	def index
		@conventions = Convention.all
	end

end
