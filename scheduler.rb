
# mini event class (not connected to db) for testing purposes
class Event

	attr_reader :length, :hosts, :name
	attr_accessor :times

	# vars: time length of event (hrs);
	# list of acceptable times to schedule event in the format [[start1, end1], [start2, end2], etc.],
		# where the start and end can encompass any length of time within the overall convention time;
	# list of hosts of event;
	# name of event (must be unique)
	def initialize(length, times, hosts, name)
		@length = length
		@times = times
		@hosts = hosts
		@name = name
	end

end


class Scheduler

	attr_reader :events, :rooms, :times, :availability

	# vars: list of Event objects to be scheduled; number of rooms available; time brackets during which convention runs
	def initialize(rooms, times)
		@rooms = rooms
		@times = times

		# initialize to all rooms available at all times
		@availability = Hash.new
		@times.each do |t|
			for i in t[0]..t[1]
				@availability[i] = @rooms
			end
		end
	end

	# publicly accessed runner for scheduler; calls backtracking algorithm & changes @events to solution
	def run(eventlist)
		solution = backtrack(eventlist)
		unless solution=="fail"
			@events = solution
			puts "success"
		else
			puts "failure"
		end
	end


	# private methods
	private

	# removes all time brackets that are smaller than event length
	def remove_times(event)
		event.times.each do |t|
			if t[1].to_i-t[0].to_i < event.length
				event.times.delete(t)
			end
		end
	end

	# determines total slack available (in hrs) in picking a time for the event
	def get_slack(event)
		remove_times(event)
		total = 0
		event.times.each do |t|
			total += (t[1].to_i - t[0].to_i)
		end
		total-event.length
	end

	# removes times in event that conflict with start & end times v1 & v2
	def trim_times(event,v1,v2)
		event.times.each do |t|
			if t[0]==v1 && t[1]==v2
				event.times.delete(t)
			elsif t[0] >= v1 && t[0] < v2
				t[0] = v2
			elsif t[1] <= v2 && t[1] > v1
				t[1] = v1
			elsif t[0] < v1 && t[1] > v2
				temp = t[1]
				t[1] = v1
				event.times.push([v2,temp])
			end
		end
	end

	# backtracking algorithm to find a successful schedule assignment
	def backtrack(elist)

		# sort events based on slack
		elist.sort! do |event1, event2|
			get_slack(event1) <=> get_slack(event2)
		end
		
		# find next event to assign (the one with the least slack) and remove from event list
		curr_event = elist.delete_at(0)

		# make array of all possible start times for current event
		start_vals = []
		curr_event.times.each do |t|
			for i in t[0]..(t[1]-curr_event.length)
				start_vals.push(i)
			end
		end

		# try each assigning each possible value until one works
		start_vals.each do |v1|
			v2 = v1+curr_event.length
			curr_event.times=([[v1,v2]])

			# uses 1 room for the chosen time value
			overbooked = false
			for i in v1..v2
				@availability[i] -= 1

				# check that it hasn't used more rooms than are available
				if @availability[i] < 0
					overbooked = true
				end
			end
			
			# if rooms are overbooked, undo availability adjustment and remove this time value from possible assignments
			if overbooked 
				for i in v1..v2
					@availability[i] += 1
				end
				start_vals.delete(v1)

				# skip the rest of this iteration of the loop
				next
			end
			
			# base case - have assigned values to every event from original list
			if elist.empty?
				return [curr_event]
			end
			
			# check for host conflicts - a host can't be at 2 events at the same time
			elist.each do |e|
				curr_event.hosts.each do |h|
					# for each event, if it shares a host with curr_event
					if e.hosts.include?(h)
						trim_times(e,v1,v2)
					end
				end
			end
			
			# re-sort & check slack; if any events have a negative amount of slack, this time assignment doesn't work
			elist.sort! do |event1, event2|
				get_slack(event1) <=> get_slack(event2)
			end
			
			if get_slack(elist[0]) >= 0

				# this time assignment works; recursively call backtrack algorithm to assign a time to the next event in the list
				result = backtrack(elist)
				return result + [curr_event] unless result=="fail"
			end
			
			# didn't return success in if statements; remove v from potential start times, reset room availability
			for i in v1..v2
				@availability[i] += 1
			end
			start_vals.delete(v1)
		end

		# went through all recursive calls without returning a success
		return "fail"
	end

end


# testing

e1 = Event.new(1, [[10,14],[34,37]], ["a","b"], "event1")
e2 = Event.new(1, [[10,12],[35,38]], ["a","c","d"], "event2")
e3 = Event.new(2, [[11,12],[38,40]], ["a","b"], "event3")
e4 = Event.new(1, [[10,14]], ["b","c","a","d"], "event4")
e5 = Event.new(2, [[12,14],[34,38]], ["c","d","a","b"], "event5")

elist = [e1,e2,e3,e4,e5]
contime = [[10,16],[34,40]]
sch = Scheduler.new(3,contime)


sch.run(elist)

sch.events.each do |e|
	puts "#{e.name}: " + e.times.to_s
end




