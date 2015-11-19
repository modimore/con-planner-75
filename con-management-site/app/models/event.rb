class Event < ActiveRecord::Base
    def as_json(opitons={})
        super(:only => [:name,:host_name,:description,:length])
    end
end
