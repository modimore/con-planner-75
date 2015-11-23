class CreateSchedules < ActiveRecord::Migration
  def change
    create_table :schedules do |t|
      t.string :convention
      t.integer :version
      t.string :event
      t.datetime :start
      t.datetime :end
      t.string :room

      t.timestamps null: false
    end
  end
end
