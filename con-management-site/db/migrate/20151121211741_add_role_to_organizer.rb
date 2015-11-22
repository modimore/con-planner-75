class AddRoleToOrganizer < ActiveRecord::Migration
  def change
    add_column :organizers, :role, :string
  end
end
