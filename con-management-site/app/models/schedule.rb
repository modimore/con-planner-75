class Schedule < ActiveRecord::Base
        def as_json(opitons={})
        super(:only => [:event,:start,:end,:room])
    end
end
