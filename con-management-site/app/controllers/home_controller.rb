class HomeController < ApplicationController

	def home; @conventions = Convention.all; end

end
