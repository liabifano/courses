puts "hi motherfucker"

class Point

  attr_accessor(:x,:y)

  def initialize(x,y)
    @x = x
    @y = y
  end

  def distFromOrigin
    Math.sqrt(@x*@x + @y*@y)
  end

  def distFromOrigin2
    Math.sqrt(x*x+y*y)
  end

end

x = Point.new(1,2)
puts x.distFromOrigin
x.x=9
puts x.distFromOrigin
puts x.distFromOrigin2



class ColorPoint < Point
  attr_accessor(:color)

  def initialize(x,y,c="clear")
    super(x,y)
    @color = c
  end

  def get_color
    @color
  end

end

k = ColorPoint.new(1,2,"green")
puts k.distFromOrigin
k.x=10
puts k.distFromOrigin
puts k.get_color