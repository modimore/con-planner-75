class HomeController < ApplicationController

	# display all conventions in system on home page
	def home; @conventions = Convention.all; end

  #Sends a lean json that has all of the convention names matching the query string
  def client_search
    @conventions = Convention.where("name LIKE ?" , "%" + params[:query] + "%")

    if @conventions.empty?
      render json: {}
    else
      results = {}
      results[:conventions] = @conventions.as_json
      render json: results
    end
  end

end
