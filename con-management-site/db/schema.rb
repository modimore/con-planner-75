# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20151123081656) do

  # These are extensions that must be enabled in order to support this database
  enable_extension "plpgsql"

  create_table "breaks", force: :cascade do |t|
    t.string   "con_name"
    t.datetime "start"
    t.datetime "end"
  end

  create_table "conventions", force: :cascade do |t|
    t.string   "name"
    t.text     "description"
    t.string   "location"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
    t.datetime "start"
    t.datetime "end"
  end

  create_table "documents", force: :cascade do |t|
    t.string   "display_name"
    t.string   "convention_name"
    t.string   "location"
    t.datetime "created_at",      null: false
    t.datetime "updated_at",      null: false
  end

  create_table "events", force: :cascade do |t|
    t.string  "name"
    t.string  "convention_name"
    t.string  "host_name"
    t.text    "description"
    t.integer "length"
  end

  create_table "hosts", force: :cascade do |t|
    t.string "name"
    t.string "convention_name"
  end

  create_table "organizers", force: :cascade do |t|
    t.string   "username"
    t.string   "convention"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
    t.string   "role"
  end

  create_table "rooms", force: :cascade do |t|
    t.string "room_name"
    t.string "convention_name"
  end

  create_table "schedules", force: :cascade do |t|
    t.string   "convention"
    t.integer  "version"
    t.string   "event"
    t.datetime "start"
    t.datetime "end"
    t.string   "room"
    t.datetime "created_at", null: false
    t.datetime "updated_at", null: false
  end

  create_table "users", force: :cascade do |t|
    t.string   "username"
    t.string   "password_digest"
    t.string   "salt"
    t.datetime "created_at",      null: false
    t.datetime "updated_at",      null: false
  end

end
