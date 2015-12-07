class Convention < ActiveRecord::Base
    def as_json(opitons={})
        super(:only => [:name,:description,:location,:start,:end])
    end
end
