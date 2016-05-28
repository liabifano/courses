# University of Washington, Programming Languages, Homework 6, hw6runner.rb

# This is the only file you turn in, so do not modify the other files as
# part of your solution.
require_relative './hw6provided'

class MyTetris < Tetris
  def set_board
    @canvas = TetrisCanvas.new
    @board = MyBoard.new(self)
    @canvas.place(@board.block_size * @board.num_rows + 3,
                  @board.block_size * @board.num_columns + 6, 24, 80)
    @board.draw
  end

  def key_bindings
    super
    @root.bind('u', proc {
      @board.rotate_clockwise
      @board.rotate_clockwise
    }) # New ex1
    @root.bind('c', proc {
      @board.set_cheat
    })
  end
end

class MyPiece < Piece
  # The constant All_My_Pieces should be declared here
  attr_accessor(:size_array)
  def initialize(point_array, board)
    super
    @size_array = point_array.first.size
  end

  def self.next_piece (board)
    MyPiece.new(All_My_Pieces.sample, board)
  end
  # your enhancements here
  All_My_Pieces = [
      [
          [[0, 0], [1, 0], [0, 1], [1, 1]]
      ],  # square (only needs one)
      rotations([[0, 0], [-1, 0], [1, 0], [0, -1]]), # T
      [
          [[0, 0], [-1, 0], [1, 0], [2, 0]], # long (only needs two)
          [[0, 0], [0, -1], [0, 1], [0, 2]]
      ],
      rotations([[0, 0], [0, -1], [0, 1], [1, 1]]), # L
      rotations([[0, 0], [0, -1], [0, 1], [-1, 1]]), # inverted L
      rotations([[0, 0], [-1, 0], [0, -1], [1, -1]]), # S
      rotations([[0, 0], [1, 0], [0, -1], [-1, -1]]),
      [
          [[-2, 0], [-1, 0], [0, 0], [1, 0],[2, 0]],
          [[0, -2], [0, -1], [0, 0], [0, 1],[0, 2]]
      ],
      rotations([[0, 0], [1, 0], [0, -1], [0, 0]]),
      rotations([[0, 0], [-1, 0], [-1, -1], [0, -1], [0,1]])

  ]
end

class MyBoard < Board
  def initialize(*)
    super
    @current_block = MyPiece.next_piece(self)
  end
  def set_cheat
    if @score > 100 && !@chitano
      @chitano = true
      @score -= 100
    end
  end

  def next_piece
    @current_block = @chitano ? MyPiece.new([[[0,0]]], self) : MyPiece.next_piece(self)
    @chitano = false
    @current_pos = nil
  end
  def store_current
    locations = @current_block.current_rotation
    displacement = @current_block.position
    (0..locations.size-1).each{|index|
      current = locations[index];
      @grid[current[1]+displacement[1]][current[0]+displacement[0]] = @current_pos[index]
    }
    remove_filled
    @delay = [@delay - 2, 80].max
  end

end