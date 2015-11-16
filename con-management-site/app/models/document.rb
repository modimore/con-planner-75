class Document < ActiveRecord::Base
    def as_json(opitons={})
        super(:only => [:display_name,:location])
    end
end
