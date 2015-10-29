class CreateDocuments < ActiveRecord::Migration
  def change
    create_table :documents do |t|
      t.string :display_name
      t.string :convention_name
      t.string :location

      t.timestamps null: false
    end
  end
end
