class CreateConventions < ActiveRecord::Migration
  def change
    create_table :conventions do |t|
      t.string :name
      t.text :description
      t.string :location
      t.datetime :start, :end
      t.timestamps null: false
    end
  end
end
