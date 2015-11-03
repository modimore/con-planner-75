class AddTimesToConvention < ActiveRecord::Migration
  def change
    add_column :conventions, :start, :datetime
  end
end
