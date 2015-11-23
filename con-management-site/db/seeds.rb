# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

ga_salt = BCrypt::Engine.generate_salt
User.create([ { username: 'GlobalAdmin', password_digest: BCrypt::Engine.hash_secret("",ga_salt), salt: ga_salt } ])
