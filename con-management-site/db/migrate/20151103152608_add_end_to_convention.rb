class AddEndToConvention < ActiveRecord::Migration
  def change
    add_column :conventions, :end, :datetime
  end
end
