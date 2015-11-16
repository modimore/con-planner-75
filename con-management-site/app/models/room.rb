class Room < ActiveRecord::Base
    def as_json(opitons={})
        super(:only => [:room_name])
    end
end
