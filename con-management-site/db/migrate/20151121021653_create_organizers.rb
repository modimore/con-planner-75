class CreateOrganizers < ActiveRecord::Migration
  def change
    create_table :organizers do |t|
      t.string :username
      t.string :convention

      t.timestamps null: false
    end
  end
end
