class CreateBreaks < ActiveRecord::Migration
  def change
    create_table :breaks do |t|
      t.string :con_name
      t.datetime :start
      t.datetime :end
    end
  end
end
