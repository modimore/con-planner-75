class AddLengthToEvent < ActiveRecord::Migration
  def change
    add_column :events, :length, :integer
  end
end
