class CreateEvents < ActiveRecord::Migration
  def change
    create_table :events do |t|
      t.string :name
      t.string :convention_name
      t.string :host_name
      t.text :description
    end
  end
end
