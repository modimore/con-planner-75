class Host < ActiveRecord::Base
    def as_json(opitons={})
        super(:only => [:name])
    end
end
