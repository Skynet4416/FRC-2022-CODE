using Plots; using PlotThemes; using BenchmarkTools;
theme(:dark)

Motor = "FALCON"        # in name
RPM = 6380              # in RPM
Diameter = 101.6         # in mm
Angle_D = 45            # in degrees
Gravity = 9.81          # in m/s^2
Resolution = 0.1        # in ms
Air_Density = 1.293  # in kg/m^3
Drag_Coefficient = 0.47 # no units
Mass = 0.267619498      # in kg
RPM_Lost = 0.25         # in %
Optimisation_Angle_Resolution = 1 # in degrees
optimisation_RPM_Resolution = 100 # in RPM

Ball_Diameter = 0.2413    # in m
Ball_Threshold = 0.0508   # in m

Hub_Height = 2.64       # in m
Hub_Distance = 7.5      # in m
Hub_Diameter = 1.22     # in m

#--/ CALCULATED /--#
power_percentage = (1-RPM_Lost)
rps = (RPM*(1-RPM_Lost)) / 60
circumference = (Diameter/1000) * pi
angle_r = deg2rad(Angle_D)
muzzle_velocity = rps * circumference # Surface speed
sim_duration = ((2muzzle_velocity*sin(angle_r))/Gravity) + 1
cross_area = pi * ((Diameter/2)/1000)^2 #m^2
drag_thing = (Air_Density * Drag_Coefficient * cross_area) /2 # kg/m
inches = round(Diameter*0.03937007874015748,digits=1)

#--/ SIMULATION - DISPLACEMENT - VOID/--#
time_points = 0:Resolution:sim_duration

x(t) = muzzle_velocity*cos(angle_r)*t
y(t) = max(muzzle_velocity*sin(angle_r).*t - 0.5*Gravity*t^2,0)

impact_distance = round(muzzle_velocity^2/Gravity*sin(2*angle_r),digits=2)
max_height = round(muzzle_velocity^2*sin(angle_r)^2/2*Gravity/100,digits=2)

#--/ SIMULATION - DISPLACEMENT - AIR/--#
function rpm_to_velocity(rpm,diameter = Diameter)
    rps = (rpm*(1-RPM_Lost)) / 60
    circumference = (diameter/1000) * pi
    mv = rps * circumference # Surface speed
end

function calculate_position_y(current_y_pos,current_velocity_y,current_acceleration_y)
    if current_velocity_y >0
        Force_y = -(Mass*Gravity + drag_thing * (current_velocity_y)^2)  
    else
        Force_y = -(Mass*Gravity - drag_thing * (current_velocity_y)^2) 
    end # calculate net force
    acceleration_y = Force_y/Mass                                  # use Newton's second law to calculate acceleration
    velocity_y = current_velocity_y + (acceleration_y+current_acceleration_y)*Resolution # update velocity 
    y_pos = max(0,current_y_pos + velocity_y *Resolution)    # update position
    [y_pos,velocity_y,current_acceleration_y]
end

function calculate_position_x(current_x_pos,current_velocity_x,current_acceleration_x)
    Force_x = - (drag_thing * (current_velocity_x)^2)                 # calculate net force
    acceleration_x = Force_x/Mass                                  # use Newton's second law to calculate acceleration
    velocity_x = current_velocity_x + (acceleration_x+current_acceleration_x)*Resolution # update velocity 
    x_pos = current_x_pos + velocity_x *Resolution           # update position
    [x_pos,velocity_x,acceleration_x]
end

function get_drag(muzzle_velocity,angle_r)
    pos_y = 0
    y_velocity = muzzle_velocity * sin(angle_r)
    y_acceleration = 0
    pos_x = 0
    x_velocity = muzzle_velocity * cos(angle_r)
    x_acceleration = 0

    arrX = [] ; arrY = []

    push!(arrX,pos_x)
    push!(arrY,pos_y)

    pos_y,y_velocity,y_acceleration = calculate_position_y(pos_y,y_velocity,y_acceleration)
    pos_x,x_velocity,x_acceleration = calculate_position_x(pos_x,x_velocity,x_acceleration)
    push!(arrX,pos_x)
    push!(arrY,pos_y)

    while pos_y > 0
        pos_y,y_velocity,y_acceleration = calculate_position_y(pos_y,y_velocity,y_acceleration)
        pos_x,x_velocity,x_acceleration = calculate_position_x(pos_x,x_velocity,x_acceleration)
        push!(arrX,pos_x)
        push!(arrY,pos_y)
    end
    [arrX,arrY]
end

function fits_in_hub(line)
    lpX = 0
    lpY = 0
    pX = 0
    pY = 0

    for i in collect(1:length(line[1]))
        lpY = pY
        lpX = pX

        pX = line[1][i]
        pY = line[2][i]
        if pX > Hub_Distance + Hub_Diameter/2
            return false
        elseif(pY < Hub_Height+Ball_Diameter+Ball_Threshold && lpY > (Hub_Height+Ball_Diameter+Ball_Threshold) && pX > ((Hub_Distance-Hub_Diameter/2)) && pX < Hub_Distance + Hub_Diameter/2 && lpX > Hub_Distance-Hub_Diameter/2)
            return true
        end
    end
    return false
end

#--/ PLOTTING /--#

function line_angle_rpm(rpm,angle)
    line = get_drag(rpm_to_velocity(rpm),deg2rad(angle))
end

function optimize()

    hline( [Hub_Height+Ball_Diameter+Ball_Threshold],label="Minimum Entry Height",linecolor=:red) # JUST TO CLEAR PLOT

    angles = collect(45:Optimisation_Angle_Resolution:90)
    done = false

    rpm = RPM*(1-RPM_Lost)

    bestRPM = -1
    bestAngle = -1

    for angle in angles
        line = line_angle_rpm(rpm,(90-angle)+45)
        while(fits_in_hub(line))
            bestRPM = rpm
            bestAngle = (90-angle)+45

            rpm -= optimisation_RPM_Resolution
            line = line_angle_rpm(rpm,(90-angle)+45)
        end
        line = line_angle_rpm(bestRPM,bestAngle)
        plot!(line[1],line[2],label="",linecolor=:blue)
    end
    line = line_angle_rpm(bestRPM,bestAngle)
    plot!(line[1],line[2],label=string(bestRPM," ",bestAngle),linecolor=:cyan)
end

# plot!(x.(time_points),y.(time_points),label="No Drag",linecolor=:red); # no friction
@btime optimize()

hline!( [Hub_Height],label="Upper Hub",linecolor=:lime)
vline!([Hub_Distance-Hub_Diameter/2],label="",linecolor=:lime)
vline!([Hub_Distance+Hub_Diameter/2],label="",linecolor=:lime)
# xlabel!(string("Distance (m) [",round(last(with_drag[1]),digits=2)," / ",impact_distance,"]"))
# ylabel!(string("Height (m) [",round(maximum(with_drag[2]),digits=2)," / ",max_height,"]"))
title!(string(Motor," ",inches,"\" ",power_percentage*100,"%"))

savefig("shooter_fig"); print("Done\n")