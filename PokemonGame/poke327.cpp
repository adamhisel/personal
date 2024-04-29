#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>
#include <sys/time.h>
#include <assert.h>
#include <curses.h>
#include <unistd.h>

#include <fstream>
#include <cstdlib> 
#include <vector>
#include <cmath> 
#include <string>
#include <random>
#include <iostream>
#include <sstream>
#include <optional>

#include "heap.h"

#define malloc(size) ({          \
  void *_tmp;                    \
  assert((_tmp = malloc(size))); \
  _tmp;                          \
})

typedef class path {
  public:
  heap_node_t *hn;
  uint8_t pos[2];
  uint8_t from[2];
  int32_t cost;
} path_t;

typedef enum dim {
  dim_x,
  dim_y,
  num_dims
} dim_t;

typedef int pair_t[num_dims]; //changed from int16_t

#define MAP_X              80
#define MAP_Y              21
#define MIN_TREES          10
#define MIN_BOULDERS       10
#define TREE_PROB          95
#define BOULDER_PROB       95
#define WORLD_SIZE         401
#define CLEAR_SCREEN "\033[2J\033[H"

#define DEFAULT_TRAINERS 10

#define MIN_TRAINERS     7
#define ADD_TRAINER_PROB 60

#define MOUNTAIN_SYMBOL       '%'
#define BOULDER_SYMBOL        '0'
#define TREE_SYMBOL           '4'
#define FOREST_SYMBOL         '^'
#define GATE_SYMBOL           '#'
#define PATH_SYMBOL           '#'
#define POKEMART_SYMBOL       'M'
#define POKEMON_CENTER_SYMBOL 'C'
#define TALL_GRASS_SYMBOL     ':'
#define SHORT_GRASS_SYMBOL    '.'
#define WATER_SYMBOL          '~'
#define ERROR_SYMBOL          '&'

#define DIJKSTRA_PATH_MAX (INT_MAX / 2)

#define PC_SYMBOL       '@'
#define HIKER_SYMBOL    'h'
#define RIVAL_SYMBOL    'r'
#define EXPLORER_SYMBOL 'e'
#define SENTRY_SYMBOL   's'
#define PACER_SYMBOL    'p'
#define SWIMMER_SYMBOL  'm'
#define WANDERER_SYMBOL 'w'

#define mappair(pair) (m->map[pair[dim_y]][pair[dim_x]])
#define mapxy(x, y) (m->map[y][x])
#define heightpair(pair) (m->height[pair[dim_y]][pair[dim_x]])
#define heightxy(x, y) (m->height[y][x])

int num_trainers = DEFAULT_TRAINERS;

class Move {
  public: 
    int id;
    std::string	identifier;
    int generation_id;
    int	type_id;
    int	power;
    int	pp;
    int	accuracy;
    int	priority;
    int target_id;
    int damage_class_id;
    int effect_id;
    int effect_chance;
    int contest_type_id;
    int contest_effect_id;
    int super_contest_effect_id;

    Move(int m_id, const std::string& m_identifier, int m_generation_id, int m_type_id,
         int m_power, int m_pp, int m_accuracy, int m_priority,
         int m_target_id, int m_damage_class_id, int m_effect_id,
         int m_effect_chance, int m_contest_type_id, int m_contest_effect_id,
         int m_super_contest_effect_id)
    : id(m_id), identifier(m_identifier), generation_id(m_generation_id), type_id(m_type_id),
      power(m_power), pp(m_pp), accuracy(m_accuracy), priority(m_priority),
      target_id(m_target_id), damage_class_id(m_damage_class_id), effect_id(m_effect_id),
      effect_chance(m_effect_chance), contest_type_id(m_contest_type_id),
      contest_effect_id(m_contest_effect_id), super_contest_effect_id(m_super_contest_effect_id) {}
};

class Pokemon {
  public: 
    int id;
    std::string	identifier;
    int species_id;
    int	height;
    int	weight;
    int	base_experience;
    int	order;
    int	is_default;
    int level = 0;
    std::vector<int> type_ids;
    std::vector<Move> pokemon_moves;
    std::vector<int> pokemon_stats;
    std::vector<int> IVs;
    std::vector<int> base_stats;
    std::string gender;
    bool is_shiny = false;
    bool fainted = false;

    Pokemon() 
    : id(0), 
      species_id(0), 
      height(0), 
      weight(0), 
      base_experience(0), 
      order(0), 
      is_default(0),
      level(0),
      gender(""),
      is_shiny(false) {
    }

    Pokemon(int p_id, const std::string& p_identifier, int p_species_id, int p_height, 
            int p_weight, int p_base_experience, int p_order, int p_is_default) 
    : id(p_id), 
      identifier(p_identifier), 
      species_id(p_species_id), 
      height(p_height), 
      weight(p_weight), 
      base_experience(p_base_experience), 
      order(p_order), 
      is_default(p_is_default) 
    {}
};

class Pokemon_Move {
  public: 
    int pokemon_id;
    int	version_group_id;
    int	move_id;
    int	pokemon_move_method_id;
    int	level;
    int	order;

    Pokemon_Move(int pm_pokemon_id, int pm_version_group_id, int pm_move_id,
                 int pm_pokemon_move_method_id, int pm_level, int pm_order)
    : pokemon_id(pm_pokemon_id), version_group_id(pm_version_group_id), move_id(pm_move_id),
      pokemon_move_method_id(pm_pokemon_move_method_id), level(pm_level), order(pm_order) {}
};

class Pokemon_Species{
  public: 
    int id;
    std::string	identifier;
    int	generation_id;
    int	evolves_from_species_id;
    int	evolution_chain_id;
    int	color_id;
    int	shape_id;
    int	habitat_id;
    int	gender_rate;
    int	capture_rate;
    int	base_happiness;
    int	is_baby;
    int	hatch_counter;
    int	has_gender_differences;
    int	growth_rate_id;
    int	forms_switchable;
    int	is_legendary;
    int	is_mythical;
    int	order;
    int	conquest_order;

    Pokemon_Species(int ps_id, const std::string& ps_identifier, int ps_generation_id,
                    int ps_evolves_from_species_id, int ps_evolution_chain_id, int ps_color_id,
                    int ps_shape_id, int ps_habitat_id, int ps_gender_rate, int ps_capture_rate,
                    int ps_base_happiness, int ps_is_baby, int ps_hatch_counter, 
                    int ps_has_gender_differences, int ps_growth_rate_id, int ps_forms_switchable,
                    int ps_is_legendary, int ps_is_mythical, int ps_order, int ps_conquest_order)
    : id(ps_id), identifier(ps_identifier), generation_id(ps_generation_id),
      evolves_from_species_id(ps_evolves_from_species_id), evolution_chain_id(ps_evolution_chain_id),
      color_id(ps_color_id), shape_id(ps_shape_id), habitat_id(ps_habitat_id),
      gender_rate(ps_gender_rate), capture_rate(ps_capture_rate), base_happiness(ps_base_happiness),
      is_baby(ps_is_baby), hatch_counter(ps_hatch_counter), has_gender_differences(ps_has_gender_differences),
      growth_rate_id(ps_growth_rate_id), forms_switchable(ps_forms_switchable),
      is_legendary(ps_is_legendary), is_mythical(ps_is_mythical), order(ps_order), 
      conquest_order(ps_conquest_order) {}
};

class Experience{
  public: 
    int growth_rate_id;
    int	level;
    int	experience;

    Experience(int e_growth_rate_id, int e_level, int e_experience)
    : growth_rate_id(e_growth_rate_id), level(e_level), experience(e_experience) {}
};

class Type_Name{
  public: 
    int type_id;
    int	local_language_id;
    std::string	name;

    Type_Name(int tn_type_id, int tn_local_language_id, const std::string& tn_name)
    : type_id(tn_type_id), local_language_id(tn_local_language_id), name(tn_name) {}
};

class Pokemon_Stats{
  public: 
    int pokemon_id;
    int	stat_id;
    int	base_stat;
    int	effort;

    Pokemon_Stats(int ps_pokemon_id, int ps_stat_id, int ps_base_stat, int ps_effort)
    : pokemon_id(ps_pokemon_id), stat_id(ps_stat_id), base_stat(ps_base_stat), effort(ps_effort) {}
};

class Stats{
  public:
    int id;
    int	damage_class_id;
    std::string	identifier;
    int	is_battle_only;
    int	game_index;

    Stats(int s_id, int s_damage_class_id, const std::string& s_identifier, 
          int s_is_battle_only, int s_game_index)
    : id(s_id), damage_class_id(s_damage_class_id), identifier(s_identifier), 
      is_battle_only(s_is_battle_only), game_index(s_game_index) {}
};

class Pokemon_Type{
  public: 
  int pokemon_id;
  int	type_id;
  int	slot;

  Pokemon_Type(int pt_pokemon_id, int pt_type_id, int pt_slot)
    : pokemon_id(pt_pokemon_id), type_id(pt_type_id), slot(pt_slot) {}
};


typedef enum __attribute__ ((__packed__)) terrain_type {
  ter_boulder,
  ter_tree,
  ter_path,
  ter_mart,
  ter_center,
  ter_grass,
  ter_clearing,
  ter_mountain,
  ter_forest,
  ter_water,
  ter_gate,
  num_terrain_types,
  ter_debug
} terrain_type_t;

typedef enum __attribute__ ((__packed__)) movement_type {
  move_hiker,
  move_rival,
  move_pace,
  move_wander,
  move_sentry,
  move_explore,
  move_swim,
  num_movement_types
} movement_type_t;

typedef enum __attribute__ ((__packed__)) character_type {
  char_pc,
  char_hiker,
  char_rival,
  char_swimmer,
  char_other,
  num_character_types
} character_type_t;

typedef class character { 
public:
  pair_t pos;
  char symbol;
  int next_turn;
  int seq_num;
  int defeated;
  std::vector<Pokemon> pokemon;
  std::vector<int> inventory_item_counts;

  character() : symbol('.'), next_turn(0), seq_num(0), defeated(0) {}
  virtual ~character() {} 
}character_t;

typedef class pc : public character {
public:
  pc() {
    symbol = '@'; 
  }
}pc_t;

typedef class npc : public character {
public:
  character_type_t ctype;
  movement_type_t mtype;
  pair_t dir;

  npc() : ctype(character_type_t::char_other), mtype(movement_type_t::move_hiker) {
    symbol = 'N'; 
  }
}npc_t;

typedef class map {
  public:
  terrain_type_t map[MAP_Y][MAP_X];
  uint8_t height[MAP_Y][MAP_X];
  character_t *cmap[MAP_Y][MAP_X];
  heap_t turn;
  int8_t n, s, e, w;
} map_t;

typedef class queue_node {
  int x, y;
  class queue_node *next;

    public:
    void setXY(int newX, int newY) { x = newX; y = newY; }
    class queue_node* getNext() const { return next; }
    void setNext(queue_node* newNext) { next = newNext; }
    int getX() const { return x; }
    int getY() const { return y; }    
 
} queue_node_t;

typedef class world {
  public:
  map_t *world[WORLD_SIZE][WORLD_SIZE];
  pair_t cur_idx;
  map_t *cur_map;
  /* Place distance maps in world, not map, since *
   * we only need one pair at any given time.     */
  
  int hiker_dist[MAP_Y][MAP_X];
  int rival_dist[MAP_Y][MAP_X];
  pc_t pc;
  int char_seq_num;
} world_t;

/* Even unallocated, a WORLD_SIZE x WORLD_SIZE array of pointers is a very *
 * large thing to put on the stack.  To avoid that, world is a global.     */
world_t world;

class RandomNumGenerator {
public:
    
    static RandomNumGenerator& getInstance() {
        static RandomNumGenerator i; 
        return i;
    }

    RandomNumGenerator(const RandomNumGenerator&) = delete;
    RandomNumGenerator& operator=(const RandomNumGenerator&) = delete;

    
    int generateInt(int min, int max) {
        std::uniform_int_distribution<> distr(min, max);
        return distr(e);
    }

private:
    std::mt19937 e; 

    RandomNumGenerator() : e(std::random_device{}()) {} 
};


std::vector<Pokemon> globalPokemon;
std::vector<Move> globalMoves;
std::vector<Pokemon_Move> globalPokemonMoves;
std::vector<Pokemon_Species> globalPokemonSpecies;
std::vector<Experience> globalExperience;
std::vector<Type_Name> globalTypeNames;
std::vector<Pokemon_Stats> globalPokemonStats;
std::vector<Stats> globalStats;
std::vector<Pokemon_Type> globalPokemonTypes;



static pair_t all_dirs[8] = {
  { -1, -1 },
  { -1,  0 },
  { -1,  1 },
  {  0, -1 },
  {  0,  1 },
  {  1, -1 },
  {  1,  0 },
  {  1,  1 },
};

/* Just to make the following table fit in 80 columns */
#define IM DIJKSTRA_PATH_MAX
/* Swimmers are not allowed to move onto paths in general, and this *
 * is governed by the swimmer movement code.  However, paths over   *
 * or adjacent to water are bridges.  They can't have inifinite     *
 * movement cost, or it throws a wrench into the turn queue.        */
int32_t move_cost[num_character_types][num_terrain_types] = {
//  boulder,tree,path,mart,center,grass,clearing,mountain,forest,water,gate
  { IM, IM, 10, 10, 10, 20, 10, IM, IM, IM, 10 },
  { IM, IM, 10, 50, 50, 15, 10, 15, 15, IM, IM },
  { IM, IM, 10, 50, 50, 20, 10, IM, IM, IM, IM },
  { IM, IM, IM, IM, IM, IM, IM, IM, IM,  7, IM },
  { IM, IM, 10, 50, 50, 20, 10, IM, IM, IM, IM },
};
#undef IM

#define rand_dir(dir) {     \
  int _i = rand() & 0x7;    \
  dir[0] = all_dirs[_i][0]; \
  dir[1] = all_dirs[_i][1]; \
}

#define is_adjacent(pos, ter)                                     \
  ((world.cur_map->map[pos[dim_y] - 1][pos[dim_x] - 1] == ter) || \
   (world.cur_map->map[pos[dim_y] - 1][pos[dim_x]    ] == ter) || \
   (world.cur_map->map[pos[dim_y] - 1][pos[dim_x] + 1] == ter) || \
   (world.cur_map->map[pos[dim_y]    ][pos[dim_x] - 1] == ter) || \
   (world.cur_map->map[pos[dim_y]    ][pos[dim_x] + 1] == ter) || \
   (world.cur_map->map[pos[dim_y] + 1][pos[dim_x] - 1] == ter) || \
   (world.cur_map->map[pos[dim_y] + 1][pos[dim_x]    ] == ter) || \
   (world.cur_map->map[pos[dim_y] + 1][pos[dim_x] + 1] == ter))

void pathfind(map_t *m);

uint32_t can_see(map_t *m, character_t *voyeur, character_t *exhibitionist)
{
  /* Application of Bresenham's Line Drawing Algorithm.  If we can draw a   *
   * line from v to e without intersecting any foreign terrain, then v can  *
   * see * e.  Unfortunately, Bresenham isn't symmetric, so line-of-sight   *
   * based on this approach is not reciprocal (Helmholtz Reciprocity).      *
   * This is a very real problem in roguelike games, and one we're going to *
   * ignore for now.  Algorithms that are symmetrical are far more          *
   * expensive.                                                             */

  /* Adapted from rlg327.  For the purposes of poke327, can swimmers see    *
   * the PC adjacent to water or on a bridge?  v is always a swimmer, and e *
   * is always the player character.                                        */

  pair_t first, second;
  pair_t del, f;
  int16_t a, b, c, i;

  first[dim_x] = voyeur->pos[dim_x];
  first[dim_y] = voyeur->pos[dim_y];
  second[dim_x] = exhibitionist->pos[dim_x];
  second[dim_y] = exhibitionist->pos[dim_y];

  if (second[dim_x] > first[dim_x]) {
    del[dim_x] = second[dim_x] - first[dim_x];
    f[dim_x] = 1;
  } else {
    del[dim_x] = first[dim_x] - second[dim_x];
    f[dim_x] = -1;
  }

  if (second[dim_y] > first[dim_y]) {
    del[dim_y] = second[dim_y] - first[dim_y];
    f[dim_y] = 1;
  } else {
    del[dim_y] = first[dim_y] - second[dim_y];
    f[dim_y] = -1;
  }

  if (del[dim_x] > del[dim_y]) {
    a = del[dim_y] + del[dim_y];
    c = a - del[dim_x];
    b = c - del[dim_x];
    for (i = 0; i <= del[dim_x]; i++) {
      if (((mappair(first) != ter_water) && (mappair(first) != ter_path)) &&
          i && (i != del[dim_x])) {
        return 0;
      }
      first[dim_x] += f[dim_x];
      if (c < 0) {
        c += a;
      } else {
        c += b;
        first[dim_y] += f[dim_y];
      }
    }
    return 1;
  } else {
    a = del[dim_x] + del[dim_x];
    c = a - del[dim_y];
    b = c - del[dim_y];
    for (i = 0; i <= del[dim_y]; i++) {
      if (((mappair(first) != ter_water) && (mappair(first) != ter_path)) &&
          i && (i != del[dim_y])) {
        return 0;
      }
      first[dim_y] += f[dim_y];
      if (c < 0) {
        c += a;
      } else {
        c += b;
        first[dim_x] += f[dim_x];
      }
    }
    return 1;
  }

  return 1;
}

static void print_map()
{
  int x, y;
  int default_reached = 0;

  std::ostringstream str;

  for(size_t i = 0; i < world.pc.pokemon.size(); i++){
    if(i + 1 == world.pc.pokemon.size()){

      str << world.pc.pokemon[i].identifier;
    }
    else{
      str << world.pc.pokemon[i].identifier << ", ";
    }
  }
  
  printw("My Caught Pokemon: ");
  printw(str.str().c_str());

  for (y = 0; y < MAP_Y; y++) {
    mvaddch(y+1, 0,'\n');
    for (x = 0; x < MAP_X; x++) {
      if (world.cur_map->cmap[y][x]) {
        mvaddch(y+1, x,(world.cur_map->cmap[y][x]->symbol));
      } else {
        switch (world.cur_map->map[y][x]) {
        case ter_boulder:
          attron(COLOR_PAIR(1));
          mvaddch(y+1, x,BOULDER_SYMBOL);
          attroff(COLOR_PAIR(1));
          break;
        case ter_mountain:
          attron(COLOR_PAIR(8));
          mvaddch(y+1, x, MOUNTAIN_SYMBOL);
          attroff(COLOR_PAIR(8));
          break;
        case ter_tree:
          attron(COLOR_PAIR(2));
          mvaddch(y+1, x, (TREE_SYMBOL));
          attroff(COLOR_PAIR(2));
          break;
        case ter_forest:
          attron(COLOR_PAIR(9));
          mvaddch(y+1, x, (FOREST_SYMBOL));
          attroff(COLOR_PAIR(9));
          break;
        case ter_path:
          attron(COLOR_PAIR(3));
          mvaddch(y+1, x, (PATH_SYMBOL));
          attroff(COLOR_PAIR(3));
          break;
        case ter_gate:
          attron(COLOR_PAIR(11));
          mvaddch(y+1, x, (GATE_SYMBOL));
          attroff(COLOR_PAIR(11));
          break;
        case ter_mart:
          attron(COLOR_PAIR(4));
          mvaddch(y+1, x, (POKEMART_SYMBOL));
          attroff(COLOR_PAIR(4));
          break;
        case ter_center:
          attron(COLOR_PAIR(5));
          mvaddch(y+1, x, (POKEMON_CENTER_SYMBOL));
          attroff(COLOR_PAIR(5));
          break;
        case ter_grass:
          attron(COLOR_PAIR(6));
          mvaddch(y+1, x, (TALL_GRASS_SYMBOL));
          attroff(COLOR_PAIR(6));
          break;
        case ter_clearing:
          attron(COLOR_PAIR(6));
          mvaddch(y+1, x, (SHORT_GRASS_SYMBOL));
          attroff(COLOR_PAIR(6));
          break;
        case ter_water:
          attron(COLOR_PAIR(10));
          mvaddch(y+1, x, (WATER_SYMBOL));
          attroff(COLOR_PAIR(10));
          break;
        default:
          mvaddch(y+1, x, (ERROR_SYMBOL));
          default_reached = 1;
          break;
        }
      }
    }
  }

  if (default_reached) {
    fprintf(stderr, "Default reached in %s\n", __FUNCTION__);
  }

  printw("\nMap Coords: %d, %d", world.cur_idx[dim_y], world.cur_idx[dim_x]);
}

static void move_hiker_func(character_t *c, pair_t dest)
{
  int min;
  int base;
  int i;
  
  base = rand() & 0x7;

  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];
  min = INT_MAX;

  
  for (i = base; i < 8 + base; i++) {
    if ((world.hiker_dist[c->pos[dim_y] + all_dirs[i & 0x7][dim_y]]
                         [c->pos[dim_x] + all_dirs[i & 0x7][dim_x]] <=
         min) &&
        !world.cur_map->cmap[c->pos[dim_y] + all_dirs[i & 0x7][dim_y]]
                            [c->pos[dim_x] + all_dirs[i & 0x7][dim_x]] &&
        c->pos[dim_x] + all_dirs[i & 0x7][dim_x] != 0 &&
        c->pos[dim_x] + all_dirs[i & 0x7][dim_x] != MAP_X - 1 &&
        c->pos[dim_y] + all_dirs[i & 0x7][dim_y] != 0 &&
        c->pos[dim_y] + all_dirs[i & 0x7][dim_y] != MAP_Y - 1) {
      dest[dim_x] = c->pos[dim_x] + all_dirs[i & 0x7][dim_x];
      dest[dim_y] = c->pos[dim_y] + all_dirs[i & 0x7][dim_y];
      min = world.hiker_dist[dest[dim_y]][dest[dim_x]];
    }
  }
}

static void move_rival_func(character_t *c, pair_t dest)
{

  int min;
  int base;
  int i;
  
  base = rand() & 0x7;

  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];
  min = INT_MAX;
  
  for (i = base; i < 8 + base; i++) {
    if ((world.rival_dist[c->pos[dim_y] + all_dirs[i & 0x7][dim_y]]
                         [c->pos[dim_x] + all_dirs[i & 0x7][dim_x]] <
         min) &&
        !world.cur_map->cmap[c->pos[dim_y] + all_dirs[i & 0x7][dim_y]]
                            [c->pos[dim_x] + all_dirs[i & 0x7][dim_x]] &&
        c->pos[dim_x] + all_dirs[i & 0x7][dim_x] != 0 &&
        c->pos[dim_x] + all_dirs[i & 0x7][dim_x] != MAP_X - 1 &&
        c->pos[dim_y] + all_dirs[i & 0x7][dim_y] != 0 &&
        c->pos[dim_y] + all_dirs[i & 0x7][dim_y] != MAP_Y - 1) {
      dest[dim_x] = c->pos[dim_x] + all_dirs[i & 0x7][dim_x];
      dest[dim_y] = c->pos[dim_y] + all_dirs[i & 0x7][dim_y];
      min = world.rival_dist[dest[dim_y]][dest[dim_x]];
    }
  }
}

static void move_pacer_func(character_t *c, pair_t dest)
{
  terrain_type_t t;

  npc_t* npc = dynamic_cast<npc_t*>(c);
  
  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];

  t = world.cur_map->map[npc->pos[dim_y] + npc->dir[dim_y]]
                        [npc->pos[dim_x] + npc->dir[dim_x]];

  if ((t != ter_path && t != ter_grass && t != ter_clearing) ||
      world.cur_map->cmap[npc->pos[dim_y] + npc->dir[dim_y]]
                         [npc->pos[dim_x] + npc->dir[dim_x]]) {
    npc->dir[dim_x] *= -1;
    npc->dir[dim_y] *= -1;
  }

  if ((t == ter_path || t == ter_grass || t == ter_clearing) &&
      !world.cur_map->cmap[npc->pos[dim_y] + npc->dir[dim_y]]
                          [npc->pos[dim_x] + npc->dir[dim_x]]) {
    dest[dim_x] = npc->pos[dim_x] + npc->dir[dim_x];
    dest[dim_y] = npc->pos[dim_y] + npc->dir[dim_y];
  }
}

static void move_wanderer_func(character_t *c, pair_t dest)
{

  npc_t* npc = dynamic_cast<npc_t*>(c);

  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];

  if ((world.cur_map->map[npc->pos[dim_y] + npc->dir[dim_y]]
                         [c->pos[dim_x] + npc->dir[dim_x]] !=
       world.cur_map->map[c->pos[dim_y]][c->pos[dim_x]]) ||
      world.cur_map->cmap[c->pos[dim_y] + npc->dir[dim_y]]
                         [c->pos[dim_x] + npc->dir[dim_x]]) {
    rand_dir(npc->dir);
  }

  if ((world.cur_map->map[c->pos[dim_y] + npc->dir[dim_y]]
                         [c->pos[dim_x] + npc->dir[dim_x]] ==
       world.cur_map->map[c->pos[dim_y]][c->pos[dim_x]]) &&
      !world.cur_map->cmap[c->pos[dim_y] + npc->dir[dim_y]]
                          [c->pos[dim_x] + npc->dir[dim_x]]) {
    dest[dim_x] = c->pos[dim_x] + npc->dir[dim_x];
    dest[dim_y] = c->pos[dim_y] + npc->dir[dim_y];
  }
}

static void move_sentry_func(character_t *c, pair_t dest)
{
  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];
}

static void move_explorer_func(character_t *c, pair_t dest)
{
  npc_t* npc = dynamic_cast<npc_t*>(c);

  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];

  if ((move_cost[char_other][world.cur_map->map[c->pos[dim_y] +
                                                npc->dir[dim_y]]
                                               [c->pos[dim_x] +
                                                npc->dir[dim_x]]] ==
       DIJKSTRA_PATH_MAX) ||
      world.cur_map->cmap[c->pos[dim_y] + npc->dir[dim_y]]
                         [c->pos[dim_x] + npc->dir[dim_x]]) {
    rand_dir(npc->dir);
  }

  if ((move_cost[char_other][world.cur_map->map[c->pos[dim_y] +
                                                npc->dir[dim_y]]
                                               [c->pos[dim_x] +
                                                npc->dir[dim_x]]] !=
       DIJKSTRA_PATH_MAX) &&
      !world.cur_map->cmap[c->pos[dim_y] + npc->dir[dim_y]]
                          [c->pos[dim_x] + npc->dir[dim_x]]) {
    dest[dim_x] = c->pos[dim_x] + npc->dir[dim_x];
    dest[dim_y] = c->pos[dim_y] + npc->dir[dim_y];
  }
}

static void move_swimmer_func(character_t *c, pair_t dest)
{
  npc_t* npc = dynamic_cast<npc_t*>(c);
  map_t *m = world.cur_map;
  pair_t dir; 

  dest[dim_x] = c->pos[dim_x];
  dest[dim_y] = c->pos[dim_y];

  if (is_adjacent(world.pc.pos, ter_water) &&
      can_see(world.cur_map, c, &world.pc)) {
    /* PC is next to this body of water; swim to the PC */

    dir[dim_x] = world.pc.pos[dim_x] - c->pos[dim_x];
    if (dir[dim_x]) {
      dir[dim_x] /= abs(dir[dim_x]);
    }
    dir[dim_y] = world.pc.pos[dim_y] - c->pos[dim_y];
    if (dir[dim_y]) {
      dir[dim_y] /= abs(dir[dim_y]);
    }

    if ((m->map[dest[dim_y] + dir[dim_y]]
               [dest[dim_x] + dir[dim_x]] == ter_water) ||
        ((m->map[dest[dim_y] + dir[dim_y]]
                [dest[dim_x] + dir[dim_x]] == ter_path) &&
         is_adjacent(((pair_t){ (dest[dim_x] + dir[dim_x]),
                                (dest[dim_y] + dir[dim_y]) }), ter_water))) {
      dest[dim_x] += dir[dim_x];
      dest[dim_y] += dir[dim_y];
    } else if ((m->map[dest[dim_y]][dest[dim_x] + dir[dim_x]] == ter_water) ||
               ((m->map[dest[dim_y]][dest[dim_x] + dir[dim_x]] == ter_path) &&
                is_adjacent(((pair_t){ (dest[dim_x] + dir[dim_x]),
                                       (dest[dim_y]) }), ter_water))) {
      dest[dim_x] += dir[dim_x];
    } else if ((m->map[dest[dim_y] + dir[dim_y]][dest[dim_x]] == ter_water) ||
               ((m->map[dest[dim_y] + dir[dim_y]][dest[dim_x]] == ter_path) &&
                is_adjacent(((pair_t){ (dest[dim_x]),
                                       (dest[dim_y] + dir[dim_y]) }),
                            ter_water))) {
      dest[dim_y] += dir[dim_y];
    }
  } else {
    /* PC is elsewhere.  Keep doing laps. */
    dir[dim_x] = npc->dir[dim_x];
    dir[dim_y] = npc->dir[dim_y];
    if ((m->map[dest[dim_y] + dir[dim_y]]
                    [dest[dim_x] + dir[dim_x]] != ter_water) ||
        !((m->map[dest[dim_y] + dir[dim_y]]
                 [dest[dim_x] + dir[dim_x]] == ter_path) &&
          is_adjacent(((pair_t) { dest[dim_x] + dir[dim_x],
                                  dest[dim_y] + dir[dim_y] }), ter_water))) {
      rand_dir(dir);
    }

    if ((m->map[dest[dim_y] + dir[dim_y]]
                    [dest[dim_x] + dir[dim_x]] == ter_water) ||
        ((m->map[dest[dim_y] + dir[dim_y]]
                [dest[dim_x] + dir[dim_x]] == ter_path) &&
         is_adjacent(((pair_t) { dest[dim_x] + dir[dim_x],
                                 dest[dim_y] + dir[dim_y] }), ter_water))) {
      dest[dim_x] += dir[dim_x];
      dest[dim_y] += dir[dim_y];
    }
  }

  if (m->cmap[dest[dim_y]][dest[dim_x]]) {
    /* Occupied.  Just be patient. */
    dest[dim_x] = c->pos[dim_x];
    dest[dim_y] = c->pos[dim_y];
  }
}

void (*move_func[num_movement_types])(character_t *, pair_t) = {
  move_hiker_func,
  move_rival_func,
  move_pacer_func,
  move_wanderer_func,
  move_sentry_func,
  move_explorer_func,
  move_swimmer_func,
};

void rand_pos(pair_t pos)
{
  pos[dim_x] = (rand() % (MAP_X - 2)) + 1;
  pos[dim_y] = (rand() % (MAP_Y - 2)) + 1;
}

int calculateManhattanDistance(int x1, int y1) {
    int dist = std::abs(x1-200) + std::abs(y1-200);
    return dist;
}

std::vector<Move> findMoves(Pokemon p) {

    std::vector<Move> availableMoves;
    for (size_t i = 0; i < globalPokemonMoves.size(); i++) {
        if (globalPokemonMoves[i].pokemon_id == p.species_id &&
            globalPokemonMoves[i].pokemon_move_method_id == 1 &&
            globalPokemonMoves[i].level <= p.level) {

            int moveId = globalPokemonMoves[i].move_id;
            availableMoves.push_back(globalMoves[moveId - 1]); 
        }
    }

    return availableMoves;
}

void levelUp(Pokemon p){
  for(size_t i = 0; i < p.pokemon_stats.size(); i++){
    int newStat;
    if(i == 0){
      newStat = ((((p.base_stats[i] * p.IVs[i]) * 2) * p.level)/100) + p.level + 10;
    }
    else{
      newStat = ((((p.base_stats[i] * p.IVs[i]) * 2) * p.level)/100) + 5;
    }
    p.pokemon_stats[i] = newStat;  
  }
}

Pokemon aquirePokemon(int id){

  auto& rng = RandomNumGenerator::getInstance();
  Pokemon p;


  if(id != 0){
    p = globalPokemon[id-1];
  }
  else{
    int randPokemon = rng.generateInt(0, 1092);
    p = globalPokemon[randPokemon];
  }

  //Level
  int manDistance = calculateManhattanDistance(world.cur_idx[dim_x], world.cur_idx[dim_y]);
  int minLevel;
  int maxLevel;

  if (manDistance <= 200) {
    minLevel = 1;
    maxLevel = std::max(1, manDistance / 2); 
  }
  else {
    minLevel = std::max(1, (manDistance - 200) / 2); 
    maxLevel = 100;
  }

  int pokeLevel = minLevel + (std::rand() % (maxLevel - minLevel + 1));

  p.level = pokeLevel;


  //Moves
  bool hasLevel = false;

  std::vector<Move> availableMoves = findMoves(p);

  while(!hasLevel){
    
    if(availableMoves.size() == 0){
      p.level = p.level + 1;
      levelUp(p);
      availableMoves = findMoves(p);
    }
    else{
      hasLevel = true;
    }
  }

  int moveSetSize = static_cast<int>(availableMoves.size());

  if(moveSetSize == 1){
    p.pokemon_moves.push_back(availableMoves[0]);
  }
  else if(moveSetSize == 2){
    p.pokemon_moves.push_back(availableMoves[0]);
    p.pokemon_moves.push_back(availableMoves[1]);
  }
  else{
    int randMove1 = rng.generateInt(0, moveSetSize);
    int randMove2 = rng.generateInt(0, moveSetSize);
    while(randMove1 == randMove2){
      randMove1 = rng.generateInt(0, moveSetSize);
      randMove2 = rng.generateInt(0, moveSetSize);
    }
    p.pokemon_moves.push_back(availableMoves[randMove1]);
    p.pokemon_moves.push_back(availableMoves[randMove2]);
  }

  //Stats
  for(size_t i = 0; i < globalPokemonStats.size(); i++){
    if(p.species_id == globalPokemonStats[i].pokemon_id){
      int finalStat;
      int IV = rng.generateInt(0, 15);
      int baseStat = globalPokemonStats[i].base_stat;
      
      p.IVs.push_back(IV);
      p.base_stats.push_back(baseStat);

      if(i == 0){
        finalStat = ((((baseStat * IV) * 2) * p.level)/100) + p.level + 10;
      }
      else{
        finalStat = ((((baseStat * IV) * 2) * p.level)/100) + 5;
      }
      p.pokemon_stats.push_back(finalStat);
    }
  }

  //Gender
  int genderVal = rng.generateInt(0, 1);
  if(genderVal == 0){
    p.gender = "Male";
  }
  else{
    p.gender = "Female";
  }

  //Shiny
  if(std::rand() % 512 == 0){
    p.is_shiny = true;
  }

  //Type
  for(size_t i = 0; i < globalPokemonTypes.size(); i++){
    if(p.id == globalPokemonTypes[i].pokemon_id){
      int type_id = globalPokemonTypes[i].type_id;
      p.type_ids.push_back(type_id);
    }
  }


  return p;

}

void new_hiker()
{
  pair_t pos;
  character_t *c;

  do {
    rand_pos(pos);
  } while (world.hiker_dist[pos[dim_y]][pos[dim_x]] >= DIJKSTRA_PATH_MAX ||
           world.cur_map->cmap[pos[dim_y]][pos[dim_x]]);

  npc *newNpc = new npc();
  newNpc->pos[dim_y] = pos[dim_y];
  newNpc->pos[dim_x] = pos[dim_x];
  newNpc->ctype = char_hiker;
  newNpc->mtype = move_hiker;
  newNpc->dir[dim_x] = 0;
  newNpc->dir[dim_y] = 0;
  newNpc->symbol = HIKER_SYMBOL;
  newNpc->next_turn = 0;
  newNpc->defeated = 0;
  newNpc->seq_num = world.char_seq_num++;
  Pokemon p = aquirePokemon(0);
  newNpc->pokemon.push_back(p);


  
  c = newNpc;

  heap_insert(&world.cur_map->turn, c);
  world.cur_map->cmap[pos[dim_y]][pos[dim_x]] = c;
}

void new_rival()
{
  pair_t pos;
  character_t *c;

  do {
    rand_pos(pos);
  } while (world.rival_dist[pos[dim_y]][pos[dim_x]] >= DIJKSTRA_PATH_MAX ||
           world.rival_dist[pos[dim_y]][pos[dim_x]] < 0        ||
           world.cur_map->cmap[pos[dim_y]][pos[dim_x]]);

  npc *newNpc = new npc();
  newNpc->pos[dim_y] = pos[dim_y];
  newNpc->pos[dim_x] = pos[dim_x];
  newNpc->ctype = char_rival;
  newNpc->mtype = move_rival;
  newNpc->dir[dim_x] = 0;
  newNpc->dir[dim_y] = 0;
  newNpc->symbol = RIVAL_SYMBOL;
  newNpc->next_turn = 0;
  newNpc->defeated = 0;
  newNpc->seq_num = world.char_seq_num++;
  Pokemon p = aquirePokemon(0);
  newNpc->pokemon.push_back(p);

  // Since npc is derived from character, this is valid
  c = newNpc;

  heap_insert(&world.cur_map->turn, c);
  world.cur_map->cmap[pos[dim_y]][pos[dim_x]] = c;
}

void new_swimmer()
{
  pair_t pos;
  character_t *c;

  do {
    rand_pos(pos);
  } while (world.cur_map->map[pos[dim_y]][pos[dim_x]] != ter_water ||
           world.cur_map->cmap[pos[dim_y]][pos[dim_x]]);

  npc *newNpc = new npc();
  newNpc->pos[dim_y] = pos[dim_y];
  newNpc->pos[dim_x] = pos[dim_x];
  newNpc->ctype = char_swimmer;
  newNpc->mtype = move_swim;
  rand_dir(newNpc->dir);
  newNpc->symbol = SWIMMER_SYMBOL;
  newNpc->next_turn = 0;
  newNpc->defeated = 0;
  newNpc->seq_num = world.char_seq_num++;
  Pokemon p = aquirePokemon(0);
  newNpc->pokemon.push_back(p);

  c = newNpc;

  heap_insert(&world.cur_map->turn, c);
  world.cur_map->cmap[pos[dim_y]][pos[dim_x]] = c;
}

void new_char_other()
{
  pair_t pos;
  character_t *c;

  do {
    rand_pos(pos);
  } while (world.rival_dist[pos[dim_y]][pos[dim_x]] >= DIJKSTRA_PATH_MAX ||
           world.rival_dist[pos[dim_y]][pos[dim_x]] < 0        ||
           world.cur_map->cmap[pos[dim_y]][pos[dim_x]]);

  npc *newNpc = new npc();
  newNpc->pos[dim_y] = pos[dim_y];
  newNpc->pos[dim_x] = pos[dim_x];
  newNpc->ctype = char_other;

  switch (rand() % 4) {
    case 0:
      newNpc->mtype = move_pace;
      newNpc->symbol = PACER_SYMBOL;
      break;
    case 1:
      newNpc->mtype = move_wander;
      newNpc->symbol = WANDERER_SYMBOL;
      break;
    case 2:
      newNpc->mtype = move_sentry;
      newNpc->symbol = SENTRY_SYMBOL;
      break;
    case 3:
      newNpc->mtype = move_explore;
      newNpc->symbol = EXPLORER_SYMBOL;
      break;
  }

  rand_dir(newNpc->dir);
  newNpc->next_turn = 0;
  newNpc->defeated = 0;
  newNpc->seq_num = world.char_seq_num++;
  Pokemon p = aquirePokemon(0);
  newNpc->pokemon.push_back(p);

  c = newNpc;

  heap_insert(&world.cur_map->turn, c);
  world.cur_map->cmap[pos[dim_y]][pos[dim_x]] = c;
}

void place_characters(int num_trainers)
{

  new_hiker();
  new_rival();
  new_swimmer();

  if(num_trainers > 3){
    for(int i = 0; i < num_trainers - 3; i++){
      
      int randNPC = rand() % 4;

      if(randNPC == 0){
        new_hiker();
      }
      if(randNPC == 1){
        new_rival();
      }
      if(randNPC == 2){
        new_char_other();
      }
      if(randNPC == 3){
        new_swimmer();
      }
    }
  }
}

int32_t cmp_char_turns(const void *key, const void *with)
{
  return ((((character_t *) key)->next_turn ==
           ((character_t *) with)->next_turn)  ?
          (((character_t *) key)->seq_num -
           ((character_t *) with)->seq_num)    :
          (((character_t *) key)->next_turn -
           ((character_t *) with)->next_turn));
}

void delete_character(void *v)
{
  character_t *character = static_cast<character_t*>(v);
  pc_t *pc = dynamic_cast<pc_t*>(character);
  npc_t *npc = dynamic_cast<npc_t*>(character);
  if (v == &world.pc) {
    delete pc;
  } else {
    delete npc;
  }
}

void init_pc()
{
  int x, y;

  do {
    x = rand() % (MAP_X - 2) + 1;
    y = rand() % (MAP_Y - 2) + 1;
  } while (world.cur_map->map[y][x] != ter_path);

  world.pc.pos[dim_x] = x;
  world.pc.pos[dim_y] = y;

  world.cur_map->cmap[y][x] = &world.pc;
  world.pc.next_turn = 0;
  world.pc.inventory_item_counts.push_back(7);
  world.pc.inventory_item_counts.push_back(12);
  world.pc.inventory_item_counts.push_back(10);

  world.pc.seq_num = world.char_seq_num++;

  heap_insert(&world.cur_map->turn, &world.pc);
}

static int32_t path_cmp(const void *key, const void *with) {
  return ((path_t *) key)->cost - ((path_t *) with)->cost;
}

static int32_t edge_penalty(int8_t x, int8_t y)
{
  return (x == 1 || y == 1 || x == MAP_X - 2 || y == MAP_Y - 2) ? 2 : 1;
}

static void dijkstra_path(map_t *m, pair_t from, pair_t to)
{
  static path_t path[MAP_Y][MAP_X], *p;
  static uint32_t initialized = 0;
  heap_t h;
  int x, y; //changed from u32int

  if (!initialized) {
    for (y = 0; y < MAP_Y; y++) {
      for (x = 0; x < MAP_X; x++) {
        path[y][x].pos[dim_y] = y;
        path[y][x].pos[dim_x] = x;
      }
    }
    initialized = 1;
  }
  
  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      path[y][x].cost = INT_MAX;
    }
  }

  path[from[dim_y]][from[dim_x]].cost = 0;

  heap_init(&h, path_cmp, NULL);

  for (y = 1; y < MAP_Y - 1; y++) {
    for (x = 1; x < MAP_X - 1; x++) {
      path[y][x].hn = heap_insert(&h, &path[y][x]);
    }
  }

  while ((p = static_cast<path_t*>(heap_remove_min(&h)))) {
    p->hn = NULL;

    if ((p->pos[dim_y] == to[dim_y]) && p->pos[dim_x] == to[dim_x]) {
      for (x = to[dim_x], y = to[dim_y];
           (x != from[dim_x]) || (y != from[dim_y]);
           p = &path[y][x], x = p->from[dim_x], y = p->from[dim_y]) {
        /* Don't overwrite the gate */
        if (x != to[dim_x] || y != to[dim_y]) {
          mapxy(x, y) = ter_path;
          heightxy(x, y) = 0;
        }
      }
      heap_delete(&h);
      return;
    }

    if ((path[p->pos[dim_y] - 1][p->pos[dim_x]    ].hn) &&
        (path[p->pos[dim_y] - 1][p->pos[dim_x]    ].cost >
         ((p->cost + heightpair(p->pos)) *
          edge_penalty(p->pos[dim_x], p->pos[dim_y] - 1)))) {
      path[p->pos[dim_y] - 1][p->pos[dim_x]    ].cost =
        ((p->cost + heightpair(p->pos)) *
         edge_penalty(p->pos[dim_x], p->pos[dim_y] - 1));
      path[p->pos[dim_y] - 1][p->pos[dim_x]    ].from[dim_y] = p->pos[dim_y];
      path[p->pos[dim_y] - 1][p->pos[dim_x]    ].from[dim_x] = p->pos[dim_x];
      heap_decrease_key_no_replace(&h, path[p->pos[dim_y] - 1]
                                           [p->pos[dim_x]    ].hn);
    }
    if ((path[p->pos[dim_y]    ][p->pos[dim_x] - 1].hn) &&
        (path[p->pos[dim_y]    ][p->pos[dim_x] - 1].cost >
         ((p->cost + heightpair(p->pos)) *
          edge_penalty(p->pos[dim_x] - 1, p->pos[dim_y])))) {
      path[p->pos[dim_y]][p->pos[dim_x] - 1].cost =
        ((p->cost + heightpair(p->pos)) *
         edge_penalty(p->pos[dim_x] - 1, p->pos[dim_y]));
      path[p->pos[dim_y]    ][p->pos[dim_x] - 1].from[dim_y] = p->pos[dim_y];
      path[p->pos[dim_y]    ][p->pos[dim_x] - 1].from[dim_x] = p->pos[dim_x];
      heap_decrease_key_no_replace(&h, path[p->pos[dim_y]    ]
                                           [p->pos[dim_x] - 1].hn);
    }
    if ((path[p->pos[dim_y]    ][p->pos[dim_x] + 1].hn) &&
        (path[p->pos[dim_y]    ][p->pos[dim_x] + 1].cost >
         ((p->cost + heightpair(p->pos)) *
          edge_penalty(p->pos[dim_x] + 1, p->pos[dim_y])))) {
      path[p->pos[dim_y]][p->pos[dim_x] + 1].cost =
        ((p->cost + heightpair(p->pos)) *
         edge_penalty(p->pos[dim_x] + 1, p->pos[dim_y]));
      path[p->pos[dim_y]    ][p->pos[dim_x] + 1].from[dim_y] = p->pos[dim_y];
      path[p->pos[dim_y]    ][p->pos[dim_x] + 1].from[dim_x] = p->pos[dim_x];
      heap_decrease_key_no_replace(&h, path[p->pos[dim_y]    ]
                                           [p->pos[dim_x] + 1].hn);
    }
    if ((path[p->pos[dim_y] + 1][p->pos[dim_x]    ].hn) &&
        (path[p->pos[dim_y] + 1][p->pos[dim_x]    ].cost >
         ((p->cost + heightpair(p->pos)) *
          edge_penalty(p->pos[dim_x], p->pos[dim_y] + 1)))) {
      path[p->pos[dim_y] + 1][p->pos[dim_x]    ].cost =
        ((p->cost + heightpair(p->pos)) *
         edge_penalty(p->pos[dim_x], p->pos[dim_y] + 1));
      path[p->pos[dim_y] + 1][p->pos[dim_x]    ].from[dim_y] = p->pos[dim_y];
      path[p->pos[dim_y] + 1][p->pos[dim_x]    ].from[dim_x] = p->pos[dim_x];
      heap_decrease_key_no_replace(&h, path[p->pos[dim_y] + 1]
                                           [p->pos[dim_x]    ].hn);
    }
  }
}

static int build_paths(map_t *m)
{
  pair_t from, to;

  /*  printf("%d %d %d %d\n", m->n, m->s, m->e, m->w);*/

  if (m->e != -1 && m->w != -1) {
    from[dim_x] = 1;
    to[dim_x] = MAP_X - 2;
    from[dim_y] = m->w;
    to[dim_y] = m->e;

    dijkstra_path(m, from, to);
  }

  if (m->n != -1 && m->s != -1) {
    from[dim_y] = 1;
    to[dim_y] = MAP_Y - 2;
    from[dim_x] = m->n;
    to[dim_x] = m->s;

    dijkstra_path(m, from, to);
  }

  if (m->e == -1) {
    if (m->s == -1) {
      from[dim_x] = 1;
      from[dim_y] = m->w;
      to[dim_x] = m->n;
      to[dim_y] = 1;
    } else {
      from[dim_x] = 1;
      from[dim_y] = m->w;
      to[dim_x] = m->s;
      to[dim_y] = MAP_Y - 2;
    }

    dijkstra_path(m, from, to);
  }

  if (m->w == -1) {
    if (m->s == -1) {
      from[dim_x] = MAP_X - 2;
      from[dim_y] = m->e;
      to[dim_x] = m->n;
      to[dim_y] = 1;
    } else {
      from[dim_x] = MAP_X - 2;
      from[dim_y] = m->e;
      to[dim_x] = m->s;
      to[dim_y] = MAP_Y - 2;
    }

    dijkstra_path(m, from, to);
  }

  if (m->n == -1) {
    if (m->e == -1) {
      from[dim_x] = 1;
      from[dim_y] = m->w;
      to[dim_x] = m->s;
      to[dim_y] = MAP_Y - 2;
    } else {
      from[dim_x] = MAP_X - 2;
      from[dim_y] = m->e;
      to[dim_x] = m->s;
      to[dim_y] = MAP_Y - 2;
    }

    dijkstra_path(m, from, to);
  }

  if (m->s == -1) {
    if (m->e == -1) {
      from[dim_x] = 1;
      from[dim_y] = m->w;
      to[dim_x] = m->n;
      to[dim_y] = 1;
    } else {
      from[dim_x] = MAP_X - 2;
      from[dim_y] = m->e;
      to[dim_x] = m->n;
      to[dim_y] = 1;
    }

    dijkstra_path(m, from, to);
  }

  return 0;
}

static int gaussian[5][5] = {
  {  1,  4,  7,  4,  1 },
  {  4, 16, 26, 16,  4 },
  {  7, 26, 41, 26,  7 },
  {  4, 16, 26, 16,  4 },
  {  1,  4,  7,  4,  1 }
};

static int smooth_height(map_t *m)
{
  int32_t i, x, y;
  int32_t s, t, p, q;
  queue_node_t *head, *tail, *tmp, *next;
  /*  FILE *out;*/
  uint8_t height[MAP_Y][MAP_X];

  memset(&height, 0, sizeof (height));

  /* Seed with some values */
  for (i = 1; i < 255; i += 20) {
    do {
      x = rand() % MAP_X;
      y = rand() % MAP_Y;
    } while (height[y][x]);
    height[y][x] = i;
    if (i == 1) {
      head = tail = new queue_node_t;
    } else {
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
    }
    tail->setNext(NULL);
    tail->setXY(x,y);
  }

  /*
  out = fopen("seeded.pgm", "w");
  fprintf(out, "P5\n%u %u\n255\n", MAP_X, MAP_Y);
  fwrite(&height, sizeof (height), 1, out);
  fclose(out);
  */
  
  /* Diffuse the vaules to fill the space */
  while (head) {
    x = head->getX();
    y = head->getY();
    i = height[y][x];

    if (x - 1 >= 0 && y - 1 >= 0 && !height[y - 1][x - 1]) {
      height[y - 1][x - 1] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x-1,y-1);
    }
    if (x - 1 >= 0 && !height[y][x - 1]) {
      height[y][x - 1] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x-1,y);
    }
    if (x - 1 >= 0 && y + 1 < MAP_Y && !height[y + 1][x - 1]) {
      height[y + 1][x - 1] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x-1,y+1);
    }
    if (y - 1 >= 0 && !height[y - 1][x]) {
      height[y - 1][x] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x,y-1);
    }
    if (y + 1 < MAP_Y && !height[y + 1][x]) {
      height[y + 1][x] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x,y+1);
    }
    if (x + 1 < MAP_X && y - 1 >= 0 && !height[y - 1][x + 1]) {
      height[y - 1][x + 1] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x+1,y-1);
    }
    if (x + 1 < MAP_X && !height[y][x + 1]) {
      height[y][x + 1] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x+1,y);
    }
    if (x + 1 < MAP_X && y + 1 < MAP_Y && !height[y + 1][x + 1]) {
      height[y + 1][x + 1] = i;
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
      tail->setNext(NULL);
      tail->setXY(x+1,y+1);
    }

    tmp = head;
    head = head->getNext();
    free(tmp);
  }

  /* And smooth it a bit with a gaussian convolution */
  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      for (s = t = p = 0; p < 5; p++) {
        for (q = 0; q < 5; q++) {
          if (y + (p - 2) >= 0 && y + (p - 2) < MAP_Y &&
              x + (q - 2) >= 0 && x + (q - 2) < MAP_X) {
            s += gaussian[p][q];
            t += height[y + (p - 2)][x + (q - 2)] * gaussian[p][q];
          }
        }
      }
      m->height[y][x] = t / s;
    }
  }
  /* Let's do it again, until it's smooth like Kenny G. */
  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      for (s = t = p = 0; p < 5; p++) {
        for (q = 0; q < 5; q++) {
          if (y + (p - 2) >= 0 && y + (p - 2) < MAP_Y &&
              x + (q - 2) >= 0 && x + (q - 2) < MAP_X) {
            s += gaussian[p][q];
            t += height[y + (p - 2)][x + (q - 2)] * gaussian[p][q];
          }
        }
      }
      m->height[y][x] = t / s;
    }
  }

  /*
  out = fopen("diffused.pgm", "w");
  fprintf(out, "P5\n%u %u\n255\n", MAP_X, MAP_Y);
  fwrite(&height, sizeof (height), 1, out);
  fclose(out);

  out = fopen("smoothed.pgm", "w");
  fprintf(out, "P5\n%u %u\n255\n", MAP_X, MAP_Y);
  fwrite(&m->height, sizeof (m->height), 1, out);
  fclose(out);
  */

  return 0;
}

static void find_building_location(map_t *m, pair_t p)
{
  do {
    p[dim_x] = rand() % (MAP_X - 3) + 1;
    p[dim_y] = rand() % (MAP_Y - 3) + 1;

    if ((((mapxy(p[dim_x] - 1, p[dim_y]    ) == ter_path)     &&
          (mapxy(p[dim_x] - 1, p[dim_y] + 1) == ter_path))    ||
         ((mapxy(p[dim_x] + 2, p[dim_y]    ) == ter_path)     &&
          (mapxy(p[dim_x] + 2, p[dim_y] + 1) == ter_path))    ||
         ((mapxy(p[dim_x]    , p[dim_y] - 1) == ter_path)     &&
          (mapxy(p[dim_x] + 1, p[dim_y] - 1) == ter_path))    ||
         ((mapxy(p[dim_x]    , p[dim_y] + 2) == ter_path)     &&
          (mapxy(p[dim_x] + 1, p[dim_y] + 2) == ter_path)))   &&
        (((mapxy(p[dim_x]    , p[dim_y]    ) != ter_mart)     &&
          (mapxy(p[dim_x]    , p[dim_y]    ) != ter_center)   &&
          (mapxy(p[dim_x] + 1, p[dim_y]    ) != ter_mart)     &&
          (mapxy(p[dim_x] + 1, p[dim_y]    ) != ter_center)   &&
          (mapxy(p[dim_x]    , p[dim_y] + 1) != ter_mart)     &&
          (mapxy(p[dim_x]    , p[dim_y] + 1) != ter_center)   &&
          (mapxy(p[dim_x] + 1, p[dim_y] + 1) != ter_mart)     &&
          (mapxy(p[dim_x] + 1, p[dim_y] + 1) != ter_center))) &&
        (((mapxy(p[dim_x]    , p[dim_y]    ) != ter_path)     &&
          (mapxy(p[dim_x] + 1, p[dim_y]    ) != ter_path)     &&
          (mapxy(p[dim_x]    , p[dim_y] + 1) != ter_path)     &&
          (mapxy(p[dim_x] + 1, p[dim_y] + 1) != ter_path)))) {
          break;
    }
  } while (1);
}

static int place_pokemart(map_t *m)
{
  pair_t p;

  find_building_location(m, p);

  mapxy(p[dim_x]    , p[dim_y]    ) = ter_mart;
  mapxy(p[dim_x] + 1, p[dim_y]    ) = ter_mart;
  mapxy(p[dim_x]    , p[dim_y] + 1) = ter_mart;
  mapxy(p[dim_x] + 1, p[dim_y] + 1) = ter_mart;

  return 0;
}

static int place_center(map_t *m)
{  pair_t p;

  find_building_location(m, p);

  mapxy(p[dim_x]    , p[dim_y]    ) = ter_center;
  mapxy(p[dim_x] + 1, p[dim_y]    ) = ter_center;
  mapxy(p[dim_x]    , p[dim_y] + 1) = ter_center;
  mapxy(p[dim_x] + 1, p[dim_y] + 1) = ter_center;

  return 0;
}

/* Chooses tree or boulder for border cell.  Choice is biased by dominance *
 * of neighboring cells.                                                   */
static terrain_type_t border_type(map_t *m, int32_t x, int32_t y)
{
  int32_t p, q;
  int32_t r, t;
  int32_t miny, minx, maxy, maxx;
  
  r = t = 0;
  
  miny = y - 1 >= 0 ? y - 1 : 0;
  maxy = y + 1 <= MAP_Y ? y + 1: MAP_Y;
  minx = x - 1 >= 0 ? x - 1 : 0;
  maxx = x + 1 <= MAP_X ? x + 1: MAP_X;

  for (q = miny; q < maxy; q++) {
    for (p = minx; p < maxx; p++) {
      if (q != y || p != x) {
        if (m->map[q][p] == ter_mountain ||
            m->map[q][p] == ter_boulder) {
          r++;
        } else if (m->map[q][p] == ter_forest ||
                   m->map[q][p] == ter_tree) {
          t++;
        }
      }
    }
  }
  
  if (t == r) {
    return rand() & 1 ? ter_boulder : ter_tree;
  } else if (t > r) {
    if (rand() % 10) {
      return ter_tree;
    } else {
      return ter_boulder;
    }
  } else {
    if (rand() % 10) {
      return ter_boulder;
    } else {
      return ter_tree;
    }
  }
}

static int map_terrain(map_t *m, int8_t n, int8_t s, int8_t e, int8_t w)
{
  int32_t i, x, y;
  queue_node_t *head, *tail, *tmp, *next;
  terrain_type_t t;
  //  FILE *out;
  int num_grass, num_clearing, num_mountain, num_forest, num_water, num_total;
  terrain_type_t type;
  int added_current = 0;
  
  num_grass = rand() % 4 + 2;
  num_clearing = rand() % 4 + 2;
  num_mountain = rand() % 2 + 1;
  num_forest = rand() % 2 + 1;
  num_water = rand() % 2 + 1;
  num_total = num_grass + num_clearing + num_mountain + num_forest + num_water;

  memset(&m->map, 0, sizeof (m->map));

  /* Seed with some values */
  for (i = 0; i < num_total; i++) {
    do {
      x = rand() % MAP_X;
      y = rand() % MAP_Y;
    } while (m->map[y][x]);
    if (i == 0) {
      type = ter_grass;
    } else if (i == num_grass) {
      type = ter_clearing;
    } else if (i == num_grass + num_clearing) {
      type = ter_mountain;
    } else if (i == num_grass + num_clearing + num_mountain) {
      type = ter_forest;
    } else if (i == num_grass + num_clearing + num_mountain + num_forest) {
      type = ter_water;
    }
    m->map[y][x] = type;
    if (i == 0) {
      head = tail = new queue_node_t;
    } else {
      next = new queue_node_t;
      tail->setNext(next);
      tail = tail->getNext();
    }
    tail->setNext(NULL);
    tail->setXY(x,y);
  }

  /*
  out = fopen("seeded.pgm", "w");
  fprintf(out, "P5\n%u %u\n255\n", MAP_X, MAP_Y);
  fwrite(&m->map, sizeof (m->map), 1, out);
  fclose(out);
  */

  /* Diffuse the vaules to fill the space */
  while (head) {
    x = head->getX();
    y = head->getY();
    t = m->map[y][x];
    
    if (x - 1 >= 0 && !m->map[y][x - 1]) {
      if ((rand() % 100) < 80) {
        m->map[y][x - 1] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x-1,y);
      } else if (!added_current) {
        added_current = 1;
        m->map[y][x] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x,y);
      }
    }

    if (y - 1 >= 0 && !m->map[y - 1][x]) {
      if ((rand() % 100) < 20) {
        m->map[y - 1][x] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x,y-1);
      } else if (!added_current) {
        added_current = 1;
        m->map[y][x] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x,y);
      }
    }

    if (y + 1 < MAP_Y && !m->map[y + 1][x]) {
      if ((rand() % 100) < 20) {
        m->map[y + 1][x] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x,y+1);
      } else if (!added_current) {
        added_current = 1;
        m->map[y][x] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x,y);
      }
    }

    if (x + 1 < MAP_X && !m->map[y][x + 1]) {
      if ((rand() % 100) < 80) {
        m->map[y][x + 1] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x+1,y);
      } else if (!added_current) {
        added_current = 1;
        m->map[y][x] = t;
        next = new queue_node_t;
        tail->setNext(next);
        tail = tail->getNext();
        tail->setNext(NULL);
        tail->setXY(x,y);
      }
    }

    added_current = 0;
    tmp = head;
    head = head->getNext();
    free(tmp);
  }

  /*
  out = fopen("diffused.pgm", "w");
  fprintf(out, "P5\n%u %u\n255\n", MAP_X, MAP_Y);
  fwrite(&m->map, sizeof (m->map), 1, out);
  fclose(out);
  */
  
  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      if (y == 0 || y == MAP_Y - 1 ||
          x == 0 || x == MAP_X - 1) {
        mapxy(x, y) = border_type(m, x, y);
      }
    }
  }

  m->n = n;
  m->s = s;
  m->e = e;
  m->w = w;

  if (n != -1) {
    mapxy(n,         0        ) = ter_gate;
    mapxy(n,         1        ) = ter_path;
  }
  if (s != -1) {
    mapxy(s,         MAP_Y - 1) = ter_gate;
    mapxy(s,         MAP_Y - 2) = ter_path;
  }
  if (w != -1) {
    mapxy(0,         w        ) = ter_gate;
    mapxy(1,         w        ) = ter_path;
  }
  if (e != -1) {
    mapxy(MAP_X - 1, e        ) = ter_gate;
    mapxy(MAP_X - 2, e        ) = ter_path;
  }

  return 0;
}

static int place_boulders(map_t *m)
{
  int i;
  int x, y;

  for (i = 0; i < MIN_BOULDERS || rand() % 100 < BOULDER_PROB; i++) {
    y = rand() % (MAP_Y - 2) + 1;
    x = rand() % (MAP_X - 2) + 1;
    if (m->map[y][x] != ter_forest &&
        m->map[y][x] != ter_path   &&
        m->map[y][x] != ter_gate) {
      m->map[y][x] = ter_boulder;
    }
  }

  return 0;
}

static int place_trees(map_t *m)
{
  int i;
  int x, y;
  
  for (i = 0; i < MIN_TREES || rand() % 100 < TREE_PROB; i++) {
    y = rand() % (MAP_Y - 2) + 1;
    x = rand() % (MAP_X - 2) + 1;
    if (m->map[y][x] != ter_mountain &&
        m->map[y][x] != ter_path     &&
        m->map[y][x] != ter_water    &&
        m->map[y][x] != ter_gate) {
      m->map[y][x] = ter_tree;
    }
  }

  return 0;
}

// New map expects cur_idx to refer to the index to be generated.  If that
// map has already been generated then the only thing this does is set
// cur_map.
static int new_map()
{
  int d, p;
  int e, w, n, s;
  int x, y;
  character_t *c;
  
  if (world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x]]) {
    world.cur_map = world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x]];
    for(int i = 0; i < MAP_Y; i++){
        for (int j = 0; j < MAP_X; j++){
            if(world.cur_map->cmap[i][j]){
                c = world.cur_map->cmap[i][j];
                c->next_turn = world.pc.next_turn;
            }
        }
    }
    return 0;
  }

  world.cur_map                                             =
    world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x]] =
    new map_t;

  smooth_height(world.cur_map);
  
  if (!world.cur_idx[dim_y]) {
    n = -1;
  } else if (world.world[world.cur_idx[dim_y] - 1][world.cur_idx[dim_x]]) {
    n = world.world[world.cur_idx[dim_y] - 1][world.cur_idx[dim_x]]->s;
  } else {
    n = 1 + rand() % (MAP_X - 2);
  }
  if (world.cur_idx[dim_y] == WORLD_SIZE - 1) {
    s = -1;
  } else if (world.world[world.cur_idx[dim_y] + 1][world.cur_idx[dim_x]]) {
    s = world.world[world.cur_idx[dim_y] + 1][world.cur_idx[dim_x]]->n;
  } else  {
    s = 1 + rand() % (MAP_X - 2);
  }
  if (!world.cur_idx[dim_x]) {
    w = -1;
  } else if (world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x] - 1]) {
    w = world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x] - 1]->e;
  } else {
    w = 1 + rand() % (MAP_Y - 2);
  }
  if (world.cur_idx[dim_x] == WORLD_SIZE - 1) {
    e = -1;
  } else if (world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x] + 1]) {
    e = world.world[world.cur_idx[dim_y]][world.cur_idx[dim_x] + 1]->w;
  } else {
    e = 1 + rand() % (MAP_Y - 2);
  }
  
  map_terrain(world.cur_map, n, s, e, w);
     
  place_boulders(world.cur_map);
  place_trees(world.cur_map);
  build_paths(world.cur_map);
  d = (abs(world.cur_idx[dim_x] - (WORLD_SIZE / 2)) +
       abs(world.cur_idx[dim_y] - (WORLD_SIZE / 2)));
  p = d > 200 ? 5 : (50 - ((45 * d) / 200));
  //  printf("d=%d, p=%d\n", d, p);
  if ((rand() % 100) < p || !d) {
    place_pokemart(world.cur_map);
  }
  if ((rand() % 100) < p || !d) {
    place_center(world.cur_map);
  }

  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      world.cur_map->cmap[y][x] = NULL;
    }
  }

  heap_init(&world.cur_map->turn, cmp_char_turns, delete_character);

  if(world.cur_idx[dim_x] == 200 && world.cur_idx[dim_y] == 200){
    init_pc();
  }
  place_characters(num_trainers);
  pathfind(world.cur_map);

  for(int i = 0; i < MAP_Y; i++){
        for (int j = 0; j < MAP_X; j++){
            if(world.cur_map->cmap[i][j]){
                c = world.cur_map->cmap[i][j];
                c->next_turn = world.pc.next_turn;
            }
        }
    }

  return 0;
}



// The world is global because of its size, so init_world is parameterless
void init_world()
{
  world.cur_idx[dim_x] = world.cur_idx[dim_y] = WORLD_SIZE / 2;
  world.char_seq_num = 0;
  new_map();
}

void delete_world()
{
  int x, y;

  for (y = 0; y < WORLD_SIZE; y++) {
    for (x = 0; x < WORLD_SIZE; x++) {
      if (world.world[y][x]) {
        free(world.world[y][x]);
        world.world[y][x] = NULL;
      }
    }
  }
}

#define ter_cost(x, y, c) move_cost[c][m->map[y][x]]

static int32_t hiker_cmp(const void *key, const void *with) {
  return (world.hiker_dist[((path_t *) key)->pos[dim_y]]
                          [((path_t *) key)->pos[dim_x]] -
          world.hiker_dist[((path_t *) with)->pos[dim_y]]
                          [((path_t *) with)->pos[dim_x]]);
}

static int32_t rival_cmp(const void *key, const void *with) {
  return (world.rival_dist[((path_t *) key)->pos[dim_y]]
                          [((path_t *) key)->pos[dim_x]] -
          world.rival_dist[((path_t *) with)->pos[dim_y]]
                          [((path_t *) with)->pos[dim_x]]);
}

void pathfind(map_t *m)
{
  heap_t h;
  uint32_t x, y;
  static path_t p[MAP_Y][MAP_X], *c;
  static uint32_t initialized = 0;

  if (!initialized) {
    initialized = 1;
    for (y = 0; y < MAP_Y; y++) {
      for (x = 0; x < MAP_X; x++) {
        p[y][x].pos[dim_y] = y;
        p[y][x].pos[dim_x] = x;
      }
    }
  }

  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      world.hiker_dist[y][x] = world.rival_dist[y][x] = DIJKSTRA_PATH_MAX;
    }
  }
  world.hiker_dist[world.pc.pos[dim_y]][world.pc.pos[dim_x]] = 
    world.rival_dist[world.pc.pos[dim_y]][world.pc.pos[dim_x]] = 0;

  heap_init(&h, hiker_cmp, NULL);

  for (y = 1; y < MAP_Y - 1; y++) {
    for (x = 1; x < MAP_X - 1; x++) {
      if (ter_cost(x, y, char_hiker) != DIJKSTRA_PATH_MAX) {
        p[y][x].hn = heap_insert(&h, &p[y][x]);
      } else {
        p[y][x].hn = NULL;
      }
    }
  }

  while ((c = static_cast<path_t*>(heap_remove_min(&h)))) {
    c->hn = NULL;
    if ((p[c->pos[dim_y] - 1][c->pos[dim_x] - 1].hn) &&
        (world.hiker_dist[c->pos[dim_y] - 1][c->pos[dim_x] - 1] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y] - 1][c->pos[dim_x] - 1] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] - 1][c->pos[dim_x] - 1].hn);
    }
    if ((p[c->pos[dim_y] - 1][c->pos[dim_x]    ].hn) &&
        (world.hiker_dist[c->pos[dim_y] - 1][c->pos[dim_x]    ] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y] - 1][c->pos[dim_x]    ] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] - 1][c->pos[dim_x]    ].hn);
    }
    if ((p[c->pos[dim_y] - 1][c->pos[dim_x] + 1].hn) &&
        (world.hiker_dist[c->pos[dim_y] - 1][c->pos[dim_x] + 1] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y] - 1][c->pos[dim_x] + 1] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] - 1][c->pos[dim_x] + 1].hn);
    }
    if ((p[c->pos[dim_y]    ][c->pos[dim_x] - 1].hn) &&
        (world.hiker_dist[c->pos[dim_y]    ][c->pos[dim_x] - 1] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y]    ][c->pos[dim_x] - 1] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y]    ][c->pos[dim_x] - 1].hn);
    }
    if ((p[c->pos[dim_y]    ][c->pos[dim_x] + 1].hn) &&
        (world.hiker_dist[c->pos[dim_y]    ][c->pos[dim_x] + 1] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y]    ][c->pos[dim_x] + 1] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y]    ][c->pos[dim_x] + 1].hn);
    }
    if ((p[c->pos[dim_y] + 1][c->pos[dim_x] - 1].hn) &&
        (world.hiker_dist[c->pos[dim_y] + 1][c->pos[dim_x] - 1] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y] + 1][c->pos[dim_x] - 1] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] + 1][c->pos[dim_x] - 1].hn);
    }
    if ((p[c->pos[dim_y] + 1][c->pos[dim_x]    ].hn) &&
        (world.hiker_dist[c->pos[dim_y] + 1][c->pos[dim_x]    ] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y] + 1][c->pos[dim_x]    ] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] + 1][c->pos[dim_x]    ].hn);
    }
    if ((p[c->pos[dim_y] + 1][c->pos[dim_x] + 1].hn) &&
        (world.hiker_dist[c->pos[dim_y] + 1][c->pos[dim_x] + 1] >
         world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker))) {
      world.hiker_dist[c->pos[dim_y] + 1][c->pos[dim_x] + 1] =
        world.hiker_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_hiker);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] + 1][c->pos[dim_x] + 1].hn);
    }
  }
  heap_delete(&h);

  heap_init(&h, rival_cmp, NULL);

  for (y = 1; y < MAP_Y - 1; y++) {
    for (x = 1; x < MAP_X - 1; x++) {
      if (ter_cost(x, y, char_rival) != DIJKSTRA_PATH_MAX) {
        p[y][x].hn = heap_insert(&h, &p[y][x]);
      } else {
        p[y][x].hn = NULL;
      }
    }
  }

  while ((c = static_cast<path_t*>(heap_remove_min(&h)))) {
    c->hn = NULL;
    if ((p[c->pos[dim_y] - 1][c->pos[dim_x] - 1].hn) &&
        (world.rival_dist[c->pos[dim_y] - 1][c->pos[dim_x] - 1] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y] - 1][c->pos[dim_x] - 1] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] - 1][c->pos[dim_x] - 1].hn);
    }
    if ((p[c->pos[dim_y] - 1][c->pos[dim_x]    ].hn) &&
        (world.rival_dist[c->pos[dim_y] - 1][c->pos[dim_x]    ] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y] - 1][c->pos[dim_x]    ] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] - 1][c->pos[dim_x]    ].hn);
    }
    if ((p[c->pos[dim_y] - 1][c->pos[dim_x] + 1].hn) &&
        (world.rival_dist[c->pos[dim_y] - 1][c->pos[dim_x] + 1] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y] - 1][c->pos[dim_x] + 1] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] - 1][c->pos[dim_x] + 1].hn);
    }
    if ((p[c->pos[dim_y]    ][c->pos[dim_x] - 1].hn) &&
        (world.rival_dist[c->pos[dim_y]    ][c->pos[dim_x] - 1] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y]    ][c->pos[dim_x] - 1] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y]    ][c->pos[dim_x] - 1].hn);
    }
    if ((p[c->pos[dim_y]    ][c->pos[dim_x] + 1].hn) &&
        (world.rival_dist[c->pos[dim_y]    ][c->pos[dim_x] + 1] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y]    ][c->pos[dim_x] + 1] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y]    ][c->pos[dim_x] + 1].hn);
    }
    if ((p[c->pos[dim_y] + 1][c->pos[dim_x] - 1].hn) &&
        (world.rival_dist[c->pos[dim_y] + 1][c->pos[dim_x] - 1] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y] + 1][c->pos[dim_x] - 1] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] + 1][c->pos[dim_x] - 1].hn);
    }
    if ((p[c->pos[dim_y] + 1][c->pos[dim_x]    ].hn) &&
        (world.rival_dist[c->pos[dim_y] + 1][c->pos[dim_x]    ] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y] + 1][c->pos[dim_x]    ] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] + 1][c->pos[dim_x]    ].hn);
    }
    if ((p[c->pos[dim_y] + 1][c->pos[dim_x] + 1].hn) &&
        (world.rival_dist[c->pos[dim_y] + 1][c->pos[dim_x] + 1] >
         world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
         ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival))) {
      world.rival_dist[c->pos[dim_y] + 1][c->pos[dim_x] + 1] =
        world.rival_dist[c->pos[dim_y]][c->pos[dim_x]] +
        ter_cost(c->pos[dim_x], c->pos[dim_y], char_rival);
      heap_decrease_key_no_replace(&h,
                                   p[c->pos[dim_y] + 1][c->pos[dim_x] + 1].hn);
    }
  }
  heap_delete(&h);
}

void print_hiker_dist()
{
  int x, y;

  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      if (world.hiker_dist[y][x] == DIJKSTRA_PATH_MAX) {
        printf("   ");
      } else {
        printf(" %02d", world.hiker_dist[y][x] % 100);
      }
    }
    printf("\n");
  }
}

void print_rival_dist()
{
  int x, y;

  for (y = 0; y < MAP_Y; y++) {
    for (x = 0; x < MAP_X; x++) {
      if (world.rival_dist[y][x] == DIJKSTRA_PATH_MAX ||
          world.rival_dist[y][x] < 0) {
        printf("   ");
      } else {
        printf(" %02d", world.rival_dist[y][x] % 100);
      }
    }
    printf("\n");
  }
}

void print_character(character_t *c)
{
  printf("%c: <%d,%d> %d (%d)\n", c->symbol, c->pos[dim_x],
         c->pos[dim_y], c->next_turn, c->seq_num);
}

void move_defeated_char(character_t* c){

  int moved = 0;
  npc_t* npc = dynamic_cast<npc_t*>(c);

  while (moved == 0) {
      char direction[4] = {'n', 's', 'e', 'w'};
      int randNum = rand() % 4;
      int new_x = c->pos[dim_x];
      int new_y = c->pos[dim_y];
      switch (direction[randNum]) {
          case 'n':
              new_y -= 1;
              break;
          case 's':
              new_y += 1;
              break;
          case 'e':
              new_x += 1;
              break;
          case 'w':
              new_x -= 1;
              break;
      }
      //changed recently
      if(new_x >= 1 && new_x < MAP_X && new_y >= 1 && new_y < MAP_Y && c->symbol == SWIMMER_SYMBOL && world.cur_map->map[new_y][new_x] == ter_water){
            world.cur_map->cmap[c->pos[dim_y]][c->pos[dim_x]] = NULL; 
            world.cur_map->cmap[new_y][new_x] = c; 
            c->next_turn += move_cost[npc->ctype][world.cur_map->map[new_y][new_x]];
            c->pos[dim_y] = new_y;
            c->pos[dim_x] = new_x;
            moved = 1; 
      }
      else if (new_x >= 1 && new_x < MAP_X && new_y >= 1 && new_y < MAP_Y &&
            (world.cur_map->map[new_y][new_x] == ter_path || world.cur_map->map[new_y][new_x] == ter_mart ||
            world.cur_map->map[new_y][new_x] == ter_center || world.cur_map->map[new_y][new_x] == ter_grass ||
            world.cur_map->map[new_y][new_x] == ter_clearing) && 
            world.cur_map->cmap[new_y][new_x] == NULL) { 

    

            
            world.cur_map->cmap[c->pos[dim_y]][c->pos[dim_x]] = NULL; 
            world.cur_map->cmap[new_y][new_x] = c; 
            c->next_turn += move_cost[npc->ctype][world.cur_map->map[new_y][new_x]];
            c->pos[dim_y] = new_y;
            c->pos[dim_x] = new_x;
            moved = 1; 
        }
  }
}

void fly(int x, int y){

    clear();
  if(x >= 0 && x < 401 && y >= 0 && y < 401){
    world.cur_idx[dim_y] = y;
    world.cur_idx[dim_x] = x;

    world.cur_map->cmap[world.pc.pos[dim_y]][world.pc.pos[dim_x]] = NULL; 

    new_map();

    int x, y;
    do {
        x = rand() % (MAP_X - 2) + 1;
        y = rand() % (MAP_Y - 2) + 1;
    } while (world.cur_map->map[y][x] != ter_path);

    world.cur_map->cmap[y][x] = &world.pc;
    world.pc.pos[dim_y] = y;
    world.pc.pos[dim_x] = x;

    print_map();

    }
    refresh();

}

Pokemon choosePokemon(int valid, Pokemon c){
  
  clear();
  move(0,0);
  printw("User choose your pokemon to battle with!\n");
  int pokeCounter = 0;

  for(size_t i = 0; i < world.pc.pokemon.size(); i++){
    if(valid == 1 && world.pc.pokemon[i].id == c.id){
      world.pc.pokemon[i] = c;
    }
    if(world.pc.pokemon[i].fainted == false){
      printw("\n(%d) %s", static_cast<int>(i+1), world.pc.pokemon[i].identifier.c_str());
    }
    else{
      printw("\n(%d) %s, (FAINTED)", static_cast<int>(i+1), world.pc.pokemon[i].identifier.c_str());
    }
    pokeCounter++;
  }

  bool chosen = false;
  Pokemon p;

  while(chosen == false){
    echo();

    char n_str[10];
    int pid;

    
    printw("\n\nPress enter when number is typed");
    printw("\nEnter pokemon number: ");
    refresh();
    getstr(n_str);

    noecho();

    pid = atoi(n_str);

    if(pid <= pokeCounter && pid > 0){
        p = world.pc.pokemon[pid-1];
        chosen = true;
    }
  }

  return p;
}

void printStats(Pokemon c, int x, int y){
  
  move(x, y);
  clrtoeol();
  printw("Level: %d", c.level);
  move(x+1, y);
  clrtoeol();
  printw("HP: %d", c.pokemon_stats[0]);
  move(x+2, y);
  clrtoeol();
  printw("Attack: %d", c.pokemon_stats[1]);
  move(x+3, y);
  clrtoeol();
  printw("Defense: %d", c.pokemon_stats[2]);
  move(x+4, y);
  clrtoeol();
  printw("Speed: %d", c.pokemon_stats[3]);
  move(x+5, y);
  clrtoeol();
  printw("Special Attack: %d", c.pokemon_stats[4]);
  move(x+6, y);
  clrtoeol();
  printw("Special Defense: %d", c.pokemon_stats[5]);

  refresh();
}

void printMoves(int highlighted, Pokemon c) {
    for (int i = 0; i < 2; i++) {
        if (i == highlighted) {
          attron(A_UNDERLINE); 
        }
        mvprintw(12 + i, 0, "%s", c.pokemon_moves[i].identifier.c_str());
        attroff(A_UNDERLINE);
    }
}

void printItems(int highlighted){
  for(int i = 0; i < 3; i++){
    if(i == highlighted){
      attron(A_UNDERLINE); 
    }
    if(i == 0){
      mvprintw(4 + i, 25, "Revives: %d", world.pc.inventory_item_counts[0]);
    }
    if(i == 1){
      mvprintw(4 + i, 25, "Potions: %d", world.pc.inventory_item_counts[1]);
    }
    if(i == 2){
      mvprintw(4 + i, 25, "Pokeballs: %d", world.pc.inventory_item_counts[2]);
    }
    attroff(A_UNDERLINE);
  }
}

bool goesFirst(int m, int rand, Pokemon p1, Pokemon p2) {

    auto& rng = RandomNumGenerator::getInstance();

    if (p1.pokemon_moves[m].priority != p2.pokemon_moves[rand].priority) {
        return p1.pokemon_moves[m].priority > p2.pokemon_moves[rand].priority;
    }

    int s1 = p1.pokemon_stats[3];
    int s2 = p2.pokemon_stats[3];

    if (s1 != s2) {
        return s1 > s2;
    }

    return rng.generateInt(0,1) == 0;
}

int applyDamage(Move m, Pokemon a, Pokemon d){
  auto& rng = RandomNumGenerator::getInstance();

  bool critical = rng.generateInt(0, 255) < (a.base_stats[3] / 2);
  float criticalVal = critical ? 1.5f : 1.0f;

  float randValDec = rng.generateInt(85, 100) / 100.0f;

  float STABVal = 1.0f;
  for (const auto& type_id : a.type_ids) {
      if (m.type_id == type_id) {
          STABVal = 1.5f;
          break;
      }
  }

  int power = m.power;

  if(m.power == INT_MAX){
    power = 1;
  }

  float damage = std::round(
      (((((2 * static_cast<float>(a.level)) / 5.0f + 2) * power * (static_cast<float>(a.pokemon_stats[1]) / d.pokemon_stats[2])) / 50.0f) + 2) * criticalVal * randValDec * STABVal
  );

  return static_cast<int>(damage);

}

bool moveHits(Move m){
  auto& rng = RandomNumGenerator::getInstance();

  int rand = rng.generateInt(0, 99);
  if(rand <= m.accuracy){
    return true;
  }
  
  return false;

}

void changeHP(Pokemon *p, int damage){
  int defendersHP = p->pokemon_stats[0] - damage;
  if(defendersHP <= 0){
    p->pokemon_stats[0] = 0;
  }
  else{ 
    p->pokemon_stats[0] = defendersHP;
  }
}


void pokemonBattle(character_t *c){

  Pokemon p = aquirePokemon(0);
  int turnCounter = 0;
  int rand;
  
  auto& rng = RandomNumGenerator::getInstance();

  if(c == &world.pc){

    clear();
    move(0, 0);

    std::string isShiny;
    if(p.is_shiny == true){
      isShiny = "Yes";
    }
    else{
      isShiny = "No";
    }

    std::ostringstream msgStream;

    msgStream << "You have encountered a: " << p.identifier << "\n"
      << "Level: " << p.level << "\nMoves: "
      << p.pokemon_moves[0].identifier << "," << p.pokemon_moves[1].identifier
      << "\nHP: " << p.pokemon_stats[0] << "\nAttack: " << p.pokemon_stats[1] << "\nDefense: " 
      << p.pokemon_stats[2] << "\nSpeed: " << p.pokemon_stats[3] << "\nSpecial Attack: " 
      << p.pokemon_stats[4] << "\nSpecial Defense: " << p.pokemon_stats[5] << "\nGender: " << p.gender << "\nShiny?: "<< isShiny
      << "\n\nProceed to battle? (Click \'y\')";

    printw(msgStream.str().c_str());

    refresh();
  
    
    while(1){
      int ch = getch();
      if(ch == 'y'){
        Pokemon dummy;
        Pokemon c = choosePokemon(0, dummy);
        move(0,0);
        clear();
        printw(c.identifier.c_str());
        move(0, 65);
        printw(p.identifier.c_str());
        printStats(c, 2, 0);
        printStats(p, 2, 65);
        mvprintw(22, 0, "\'i\'-to open inventory");
        mvprintw(23, 0, "\'s\'-to switch pokemon");
        mvprintw(24, 0, "\'r\'-to flee");
        mvprintw(10, 0, "Choose Next Move");
        int highlighted = 0;
        bool pcFirst = false;
        bool pcdefeated = false;
        bool npcdefeated = false;
        bool endgame = false;
        bool flee = false;
        bool fleeClicked = false;
        int chh = ERR;
        int highlighted2;
        const int choices = 3;
        int pcOriginalHP;
        int npcOriginalHP;;
        printMoves(highlighted, c);

        int ch = ERR;
        while (ch != 'q') {
          bool pcMoveHits = false;
          bool npcMoveHits = false;
          ch = getch();
            switch (ch) {
              case 'q':
                break;
              case KEY_UP:
                if(pcdefeated || npcdefeated || c.fainted){
                  break;
                }
                highlighted = 0;  
                break;
              case KEY_DOWN:
                if(pcdefeated || npcdefeated || c.fainted){
                  break;
                }
                highlighted = 1;
                break;
              case 10:
                if(pcdefeated || npcdefeated || c.fainted){
                  break;
                }
                rand = rng.generateInt(0, 1);
                if(!(p.pokemon_moves.size() > 1)){
                  rand = 0;
                }
                pcFirst = goesFirst(highlighted, rand, c, p);
                if(pcFirst == true){

                    pcMoveHits = moveHits(c.pokemon_moves[highlighted]);
                    if(pcMoveHits){
                      int damage = applyDamage(c.pokemon_moves[highlighted], c, p);
                      changeHP(&p, damage);
                      attron(A_STANDOUT);
                      mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str(), damage);
                      turnCounter++;
                      attroff(A_STANDOUT);
                      if(p.pokemon_stats[0] == 0){
                        npcdefeated = true;
                        mvprintw(12+turnCounter, 25, "Your %s defeated the wild %s! Go catch it!", c.identifier.c_str(),p.identifier.c_str());
                        turnCounter++;
                        break;
                      }
                    }
                    else{
                      attron(A_STANDOUT);
                      mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str());
                      turnCounter++;
                      attroff(A_STANDOUT);
                    }
                    usleep(125000);
                    npcMoveHits = moveHits(p.pokemon_moves[rand]);
                    if(npcMoveHits){
                      int damage = applyDamage(p.pokemon_moves[rand], p, c);
                      changeHP(&c, damage);
                      mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str(), damage);
                      turnCounter++;
                      if(c.pokemon_stats[0] == 0){
                        pcdefeated = true;
                        c.fainted = true;
                        mvprintw(12+turnCounter, 25, "The wild %s defeated your %s! Switch or revive pokemon!", p.identifier.c_str(),c.identifier.c_str());
                        turnCounter++;
                        break;
                      }
                    }
                    else{
                      mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str());
                      turnCounter++;
                    }
                  } 
                  else{
                    npcMoveHits = moveHits(p.pokemon_moves[rand]);
                    if(npcMoveHits){
                      int damage = applyDamage(p.pokemon_moves[rand], p, c);
                      changeHP(&c, damage);
                      mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str(), damage);
                      turnCounter++;
                      if(c.pokemon_stats[0] == 0){
                        pcdefeated = true;
                        c.fainted = true;
                        mvprintw(12+turnCounter, 25, "The wild %s defeated your %s! Switch or revive pokemon!", p.identifier.c_str(),c.identifier.c_str());
                        turnCounter++;
                        break;
                      }
                    }
                    else{
                      mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str());
                      turnCounter++;
                    }
                    usleep(125000);
                    pcMoveHits = moveHits(c.pokemon_moves[highlighted]);
                    if(pcMoveHits){
                      int damage = applyDamage(c.pokemon_moves[highlighted], c, p);
                      changeHP(&p, damage);
                      attron(A_STANDOUT);
                      mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str(), damage);
                      turnCounter++;
                      attroff(A_STANDOUT);
                      if(p.pokemon_stats[0] == 0){
                        npcdefeated = true;
                        mvprintw(12+turnCounter, 25, "Your %s defeated the wild %s! Go catch it!", c.identifier.c_str(),p.identifier.c_str());
                        turnCounter++;
                        break;
                      }
                    }
                    else{
                      attron(A_STANDOUT);
                      mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str());
                      turnCounter++;
                      attroff(A_STANDOUT);
                    }
                  }
                break;
              case 'i': 
                mvprintw(2, 25, "Inventory:");
                chh = ERR;
                highlighted2 = 0;
                pcOriginalHP = ((((c.base_stats[0] * c.IVs[0]) * 2) * c.level) / 100) + c.level + 10;
                npcOriginalHP = ((((p.base_stats[0] * p.IVs[0]) * 2) * p.level) / 100) + p.level + 10;
                printItems(highlighted2);
                mvprintw(9, 25, "Press 'c' to close.");
                
                while (chh != 'c') {
                    chh = getch();
                    switch (chh) {
                        case KEY_UP:
                          highlighted2 = (highlighted2 - 1 + choices) % choices;
                          printItems(highlighted2);
                          break;
                        case KEY_DOWN:
                          highlighted2 = (highlighted2 + 1) % choices;
                          printItems(highlighted2);
                          break;
                        case 10:
                            switch (highlighted2) {
                              case 0:
                                if (c.fainted) {
                                    c.pokemon_stats[0] = pcOriginalHP / 2;
                                    world.pc.inventory_item_counts[0]--;
                                    pcdefeated = false;
                                    c.fainted = false;
                                    printStats(c, 2, 0);
                                    printStats(p, 2, 65);
                                    mvprintw(8, 25, "Revived to %d HP.", c.pokemon_stats[0]);
                                } else {
                                    mvprintw(8, 25, "Pokemon is not fainted.");
                                }
                                break;
                              case 1:
                                if (c.pokemon_stats[0] < pcOriginalHP) {
                                    if(c.fainted){
                                      mvprintw(8, 25, "Pokemon is fainted, revive it first.");
                                      break;
                                    }
                                    int healAmount; 
                                    if(pcOriginalHP - c.pokemon_stats[0] > 20){
                                      healAmount = c.pokemon_stats[0] + 20;
                                    }
                                    else{
                                      healAmount = pcOriginalHP;
                                    }
                                    c.pokemon_stats[0] = healAmount;
                                    world.pc.inventory_item_counts[1]--;
                                    pcdefeated = false;
                                    printStats(c, 2, 0);
                                    printStats(p, 2, 65);
                                    mvprintw(8, 25, "Healed for %d HP.", healAmount);
                                } else {
                                    mvprintw(8, 25, "Pokemon is at full health.");
                                }
                                break;
                              case 2:
                                if (p.pokemon_stats[0] == 0) {
                                    if(world.pc.pokemon.size() < 6){
                                      p.pokemon_stats[0] = npcOriginalHP;
                                      world.pc.inventory_item_counts[2]--;
                                      world.pc.pokemon.push_back(p);
                                      clear();
                                      mvprintw(0, 0, "You captured %s! CONGRATS!", p.identifier.c_str());
                                    }
                                    else{
                                      clear();
                                      mvprintw(0, 0, "The wild %s, escaped from your pokeball and fled. Better luck next time!", p.identifier.c_str());
                                    }
                                    chh = 'c';
                                    endgame = true;
                                } else {
                                    mvprintw(8, 25, "You have not defeated this pokemon yet.");
                                }
                                break;
                            }
                        clrtoeol();
                        break;
                    }
                  mvprintw(9, 25, "Press 'c' to close.");
                }
            move(9, 25);
            clrtoeol();
            break;
            case 's':
            if(!c.fainted){
              c = choosePokemon(1, c);
              if(!c.fainted){
                pcdefeated = false;
              }
              mvprintw(22, 0, "\'i\'-to open inventory");
              mvprintw(23, 0, "\'s\'-to switch pokemon");
              mvprintw(24, 0, "\'r\'-to flee");
              turnCounter = 0;
            }
            else{
              mvprintw(12+turnCounter, 25, "Must flee fight your pokemon has fainted");
            }
              break; 
            case 'r':
              int fleeProb;

              if(c.fainted){
                flee = true;
              }
              else{
                fleeProb = rng.generateInt(0,65);
                if(fleeProb < 100 && fleeClicked == false){
                  flee = true;
                }
                else{
                  fleeClicked = true;
                  mvprintw(12+turnCounter, 25, "Cannot flee this battle!");
                  turnCounter++;
                }
              }
              break;
          }
          if(flee){
            clear();
            mvprintw(10, 0, "Press \'q\' to leave.");
            refresh();
          }
          else if(endgame){
            printStats(p, 2, 0);
            mvprintw(10, 0, "Press \'q\' to leave.");
            refresh();
          }
          else{
            if(turnCounter >= 30){
              turnCounter = 0;
            }
            move(0,0);
            clrtoeol();
            printw(c.identifier.c_str());
            move(0, 65);
            printw(p.identifier.c_str());
            printStats(c, 2, 0);
            printStats(p, 2, 65);
            mvprintw(22, 0, "\'i\'-to open inventory");
            mvprintw(23, 0, "\'s\'-to switch pokemon");
            mvprintw(24, 0, "\'r\'-to flee");
            mvprintw(10, 0, "Choose Next Move");
            printMoves(highlighted, c);
            refresh();
          }
        }
        for(size_t i = 0; i < world.pc.pokemon.size(); i++){
          if(world.pc.pokemon[i].id == c.id){
            world.pc.pokemon[i] = c;
          }
        }
        move(0,0);
        clear();
        break;
      }
    }
  }
  else{
    try {
    if (c->pokemon.size() < 6) {
        int randChance = rng.generateInt(0, 99);
        std::cout << "Random chance generated: " << randChance << std::endl;
        if (randChance < 50) {
            std::cout << "Adding Pokemon to NPC. Current count: " << c->pokemon.size() << std::endl;
            c->pokemon.push_back(p);
            std::cout << "Pokemon added. New count: " << c->pokemon.size() << std::endl;
        }
    }
} catch (const std::exception& e) {
    std::cerr << "Exception caught: " << e.what() << std::endl;
    // Additional logging or handling
}
  }
}

bool runFight(character_t *n, int op) {
  move(0, 0);
  clear();
  
  int opCount = 0;
  for(size_t j = 0; j < n->pokemon.size(); j++){
    opCount++;
  }
  
  Pokemon p;
  auto& rng = RandomNumGenerator::getInstance();
  p = n->pokemon[op];


  int turnCounter = 0;
  int rand;
  
  Pokemon c = world.pc.pokemon[0];
    
  printw(c.identifier.c_str());
  move(0, 65);
  printw(p.identifier.c_str());
  printStats(c, 2, 0);
  printStats(p, 2, 65);
  mvprintw(23, 0, "\'s\'-to switch pokemon");
  mvprintw(10, 0, "Choose Next Move");
  int highlighted = 0;
  bool pcFirst = false;
  bool pcdefeated = false;
  bool npcdefeated = false;

  printMoves(highlighted, c);
            

  int ch = ERR;
      while (1) {
        bool pcMoveHits = false;
        bool npcMoveHits = false;
        ch = getch();
        switch (ch) {
          case KEY_UP:
            if(pcdefeated || npcdefeated || c.fainted){
              break;
            }
            highlighted = 0;  
            break;
          case KEY_DOWN:
            if(pcdefeated || npcdefeated || c.fainted){
              break;
            }
            highlighted = 1;
            break;
          case 10:
            if(pcdefeated || npcdefeated || c.fainted){
              break;
            }
            rand = rng.generateInt(0, 1);
            if(!(p.pokemon_moves.size() > 1)){
              rand = 0;
            }
            pcFirst = goesFirst(highlighted, rand, c, p);
            if(pcFirst == true){

              pcMoveHits = moveHits(c.pokemon_moves[highlighted]);
              if(pcMoveHits){
                int damage = applyDamage(c.pokemon_moves[highlighted], c, p);
                changeHP(&p, damage);
                attron(A_STANDOUT);
                mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str(), damage);
                turnCounter++;
                attroff(A_STANDOUT);
                if(p.pokemon_stats[0] == 0){
                  npcdefeated = true;
                  mvprintw(12+turnCounter, 25, "Your %s defeated the wild %s! Go catch it!", c.identifier.c_str(),p.identifier.c_str());
                  turnCounter++;
                  break;
                }
              }
              else{
                attron(A_STANDOUT);
                mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str());
                turnCounter++;
                attroff(A_STANDOUT);
              }
              usleep(125000);
              npcMoveHits = moveHits(p.pokemon_moves[rand]);
              if(npcMoveHits){
                int damage = applyDamage(p.pokemon_moves[rand], p, c);
                changeHP(&c, damage);
                mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str(), damage);
                turnCounter++;
                if(c.pokemon_stats[0] == 0){
                  pcdefeated = true;
                  c.fainted = true;
                  mvprintw(12+turnCounter, 25, "The wild %s defeated your %s! Switch or revive pokemon!", p.identifier.c_str(),c.identifier.c_str());
                  turnCounter++;
                  break;
                }
              }
              else{
                mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str());
                turnCounter++;
              }
            } 
            else{
              npcMoveHits = moveHits(p.pokemon_moves[rand]);
              if(npcMoveHits){
                int damage = applyDamage(p.pokemon_moves[rand], p, c);
                changeHP(&c, damage);
                mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str(), damage);
                turnCounter++;
                if(c.pokemon_stats[0] == 0){
                  pcdefeated = true;
                  c.fainted = true;
                  mvprintw(12+turnCounter, 25, "The wild %s defeated your %s! Switch or revive pokemon!", p.identifier.c_str(),c.identifier.c_str());
                  turnCounter++;
                  break;
                }
              }
              else{
                mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", p.identifier.c_str(), p.pokemon_moves[rand].identifier.c_str());
                turnCounter++;
              }
              usleep(125000);
              pcMoveHits = moveHits(c.pokemon_moves[highlighted]);
              if(pcMoveHits){
                int damage = applyDamage(c.pokemon_moves[highlighted], c, p);
                changeHP(&p, damage);
                attron(A_STANDOUT);
                mvprintw(12+turnCounter, 25, "%s, uses %s and hits for %d damage!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str(), damage);
                turnCounter++;
                attroff(A_STANDOUT);
                if(p.pokemon_stats[0] == 0){
                  npcdefeated = true;
                  mvprintw(12+turnCounter, 25, "Your %s defeated the wild %s! Go catch it!", c.identifier.c_str(),p.identifier.c_str());
                  turnCounter++;
                  break;
                }
              }
              else{
                attron(A_STANDOUT);
                mvprintw(12+turnCounter, 25, "%s, uses %s and misses!", c.identifier.c_str(), c.pokemon_moves[highlighted].identifier.c_str());
                turnCounter++;
                attroff(A_STANDOUT);
              }
            }
          break; 
        case 's':
          c = choosePokemon(1, c);
          if(!c.fainted){
            pcdefeated = false;
          }
          mvprintw(23, 0, "\'s\'-to switch pokemon");
          turnCounter = 0;
          break;
        }

        int pcfaintedCount = 0;
        int pcPokeCount = 0;

        //pc
        for(size_t i = 0; i < world.pc.pokemon.size(); i++){
          if(world.pc.pokemon[i].fainted){
            pcfaintedCount++;
          }
          pcPokeCount++;
        }
        if(pcfaintedCount == pcPokeCount){
          return false;
        }

        int npcfaintedCount = 0;
        int npcPokeCount = 0;
        
        //npc
        for(size_t i = 0; i < n->pokemon.size(); i++){
          if(n->pokemon[i].fainted){
            npcfaintedCount++;
          }
          npcPokeCount++;
        }
        if(npcfaintedCount == npcPokeCount){
          return true;
        }
        
    
          if(turnCounter >= 30){
            turnCounter = 0;
          }
          move(0,0);
          clrtoeol();
          printw(c.identifier.c_str());
          move(0, 65);
          printw(p.identifier.c_str());
          printStats(c, 2, 0);
          printStats(p, 2, 65);
          mvprintw(23, 0, "\'s\'-to switch pokemon");
          mvprintw(10, 0, "Choose Next Move");
          printMoves(highlighted, c);
          refresh();
      }
}



void move_pc(char ch) {
    character_t *pc = &world.pc;
    int new_x = pc->pos[dim_x];
    int new_y = pc->pos[dim_y];


    switch (ch) {
        case '7': case 'y': --new_x; --new_y; break;
        case '8': case 'k':          --new_y; break;
        case '9': case 'u': ++new_x; --new_y; break;
        case '6': case 'l': ++new_x;          break;
        case '3': case 'n': ++new_x; ++new_y; break;
        case '2': case 'j':          ++new_y; break;
        case '1': case 'b': --new_x; ++new_y; break;
        case '4': case 'h': --new_x;          break;
        default: return;  
    }
    
    if (new_x < 0 || new_x >= MAP_X || new_y < 0 || new_y >= MAP_Y) {
        return;
    }

    terrain_type_t t = world.cur_map->map[new_y][new_x];
    character_t *opp = world.cur_map->cmap[new_y][new_x];

    if(t == ter_gate){

        if(new_x == MAP_X - 1 || new_x == 0 || new_y == MAP_Y-1 || new_y == 0){

            world.cur_map->cmap[pc->pos[dim_y]][pc->pos[dim_x]] = NULL;
            pc->pos[dim_x] = new_x;
            pc->pos[dim_y] = new_y;
            return;
        }
        
    }
    if(t == ter_mart){
      world.pc.inventory_item_counts[0] = 7;
      world.pc.inventory_item_counts[0] = 12;
      world.pc.inventory_item_counts[0] = 10;
    }
    if(t == ter_center){
      for(size_t i = 0; i < world.pc.pokemon.size(); i++){
        world.pc.pokemon[i].pokemon_stats[0] = ((((world.pc.pokemon[i].base_stats[0] * world.pc.pokemon[i].IVs[0]) * 2) * world.pc.pokemon[i].level) / 100) + world.pc.pokemon[i].level + 10;
      }
    }

    if(t == ter_grass){
      
      auto& rng = RandomNumGenerator::getInstance();

      int encounterInt= rng.generateInt(0, 99);

      if(encounterInt < 10){
        pokemonBattle(pc);
      }
    }
  
    if (!(t == ter_path || t == ter_mart || t == ter_center || t== ter_grass || t == ter_clearing || t == ter_gate)) {
        return; 
    }
    else if (opp) {
        if (opp->defeated == 0) {
          bool pcWon = false;
          for(size_t i = 0; i < opp->pokemon.size(); i++){
            pcWon = runFight(opp, static_cast<int>(i));  
          }
          opp->defeated = 1;
           move(23,0);  
          if(pcWon == true){
            printw("You Won!!");
          }
          else{
            printw("You Lost!!");
          }
        }
        return;  
    }

    
    world.cur_map->cmap[pc->pos[dim_y]][pc->pos[dim_x]] = NULL;
    world.cur_map->cmap[new_y][new_x] = pc;
    pc->pos[dim_x] = new_x;
    pc->pos[dim_y] = new_y;
    pc->next_turn += move_cost[char_pc][t];
}


void enter_building() {
  clear();
  refresh();
  if(world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] == ter_mart) {
    printw("Welcome to the PokeMart, we currently aren't selling anything, sorry. Come back another time!");
  }
  else {
    printw("Welcome to the PokeCenter, we currently aren't selling anything, sorry. Come back another time!");
  }

  printw("\n\nPress \'<\' to exit");
  refresh();  
  getch();    
}


void display_trainers(int offset) {
  clear();

  int max_lines = getmaxy(stdscr) - 2;
  int line_counter = 0;

  character_t* characters[num_trainers];
  int iter = 0;
  

  for (int y = 0; y < MAP_Y;y++) {
    for (int x = 0; x < MAP_X; x++) {
      if (world.cur_map->cmap[y][x] && world.cur_map->cmap[y][x] != &world.pc && iter < num_trainers) {
        characters[iter++] = world.cur_map->cmap[y][x];
      }
    }
  }

  move(0, 0);

  for (int i = offset; i < num_trainers && line_counter < max_lines; i++) {
    character_t *c = characters[i];
    npc_t* npc = dynamic_cast<npc_t*>(c);
    if (c && c != &world.pc && npc) {
      int x = c->pos[dim_x] - world.pc.pos[dim_x];
      int y = c->pos[dim_y] - world.pc.pos[dim_y];
      const char *x_direction = x >= 0 ? "east" : "west";
      const char *y_direction = y >= 0 ? "south" : "north";

      move(line_counter + 1, 0);
      printw("%c, %d %s and %d %s\n", c->symbol, abs(y), y_direction, abs(x), x_direction);
      line_counter++;
    }
  }

  refresh();
}





void user_commands(char ch){

  int max_lines = getmaxy(stdscr) - 2;
  int trainer_list_offset = 0;

  int new_map_x = 0;
  int new_map_y = 0;
  int x = 0;
  int y = 0;

  if(ch == '7' || ch == 'y' || ch == '8' || ch == 'k' || ch == '9'|| ch == 'u'|| ch == '6' 
  || ch == 'l'||  ch == '3' || ch == 'n' || ch == '2' || ch == 'j'|| ch == '1'
  || ch == 'b'|| ch == '4'|| ch == 'h'){
    move_pc(ch);
        
        x = world.pc.pos[dim_x];
        y = world.pc.pos[dim_y];


        if(x == MAP_X - 1 || x == 0 || y == MAP_Y-1 || y == 0){

            if(x == MAP_X - 1){
                world.cur_idx[dim_x]++;
                new_map_x = 1;
                new_map_y = world.pc.pos[dim_y];

            }
            if(x == 0){
                world.cur_idx[dim_x]--;
                new_map_x = MAP_X - 2;
                new_map_y = world.pc.pos[dim_y];
            }
            if(y == MAP_Y - 1){
                world.cur_idx[dim_y]++;
                new_map_x = world.pc.pos[dim_x];
                new_map_y = 1;
            }
            if(y == 0){
                world.cur_idx[dim_y]--;
                new_map_x = world.pc.pos[dim_x];
                new_map_y = MAP_Y - 2;
                
            }
            

            new_map();
            world.pc.pos[dim_x] = new_map_x;
            world.pc.pos[dim_y] = new_map_y;
            world.cur_map->cmap[new_map_y][new_map_x] = &world.pc;
            // world.pc.next_turn = 0;

            refresh();
        }
  }
  else if(ch == '>' && (world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] == ter_mart || world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] == ter_center)){
    enter_building();
  }
  else if(ch == '<' && (world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] == ter_mart || world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]] == ter_center)){
    clear();
    print_map();
    refresh();
  }
  else if(ch == '5' || ch == ' ' || ch == '.'){
    world.pc.next_turn += move_cost[char_pc][world.cur_map->map[world.pc.pos[dim_y]][world.pc.pos[dim_x]]];
  }
  else if(ch == 't'){
    display_trainers(trainer_list_offset);
   while (1) {
      ch = getch();
      if (ch == KEY_UP) {
        if (trainer_list_offset > 0) {
          trainer_list_offset--;
          clear();
          display_trainers(trainer_list_offset);
          refresh();
        }
      } else if (ch == KEY_DOWN) {
        if (trainer_list_offset < num_trainers - max_lines) {
          trainer_list_offset++;
          clear();
          display_trainers(trainer_list_offset);
          refresh();
        }
      } else if (ch == 27) {
        break;
      }
    }
  
    refresh();
    }
    else if(ch == 'f'){
        echo();

        char x_str[10], y_str[10]; 
        int x, y;
        clear();

        printw("Press enter when number is typed");
        printw("\nEnter x value: ");
        refresh();
        getstr(x_str);
        
        clear();
        move(0,0);
        printw("Press enter when number is typed");
        printw("\nEnter y value: ");
        refresh();
        getstr(y_str);

        noecho();

        x = atoi(x_str);
        y = atoi(y_str);

        clear();
        printw("Fly to x: %d and y: %d?", x, y);

        printw("\n\nPress \'c\' to confirm, press \'esc\' to cancel");
        refresh();

        while (1) {
            ch = getch();
        if(ch == 'c'){
            fly(x, y);
            break;
        }
        if(ch == 27){
            clear();
            print_map();
            refresh();
            break;
        }
        }
    }
}

void game_loop() {
  character_t *c;
  pair_t d;
  int ch = ERR;

  initscr();

  if (has_colors() == FALSE) {
    endwin();
    printf("Your terminal does not support color\n");
    exit(1);
  }

  start_color();

  // Define color pairs
  init_pair(1, COLOR_WHITE, COLOR_BLACK);  // boulders: white on black
  init_pair(2, COLOR_GREEN, COLOR_BLACK);  // trees: green on black
  init_pair(3, COLOR_YELLOW, COLOR_BLACK); // paths: yellow on black
  init_pair(4, COLOR_CYAN, COLOR_BLACK);   // mart: cyan on black
  init_pair(5, COLOR_RED, COLOR_BLACK);    // center: red on black
  init_pair(6, COLOR_GREEN, COLOR_BLACK);  // grass: green on black (same as trees, or change)
  init_pair(7, COLOR_WHITE, COLOR_GREEN);  // clearing: white on green
  init_pair(8, COLOR_WHITE, COLOR_BLUE);   // mountain: white on blue
  init_pair(9, COLOR_BLACK, COLOR_GREEN);  // forest: black on green
  init_pair(10, COLOR_BLUE, COLOR_BLACK);  // water: blue on black
  init_pair(11, COLOR_MAGENTA, COLOR_BLACK);  // gate: purple on black
  init_pair(12, COLOR_YELLOW, COLOR_BLACK);  // shiny: yellow on black


  cbreak();
  noecho();
  keypad(stdscr, TRUE);
  clear();

  printw("Choose your starter Pokemon: \n\n(1) Bulbasaur\n(2) Charmander\n(3) Squirtle");
  refresh();

  while(1) {
      ch = getch();
      Pokemon p;
      if(ch == '1' || ch == '2' || ch == '3') {
        if(ch == '1'){
          p = aquirePokemon(1);
        }
        else if(ch == '2'){
          p = aquirePokemon(4);
        }
        else if(ch == '3'){
          p = aquirePokemon(7);
        }
          world.pc.pokemon.push_back(p);
          break;
      } else if(ch == 27) {
          printw("\nYou cannot exit without selecting a Pokemon! Please choose 1, 2, or 3.\n");
          refresh();
      }
  }
  clear();
  move(0,0);
  print_map();

  auto& rng = RandomNumGenerator::getInstance();


  while (1) {
    if (ch != 'Q') {
      c = static_cast<character_t*>(heap_remove_min(&world.cur_map->turn));

      if (c == &world.pc) {
        do {
          ch = getch();
          if (ch == 'Q') {
            break;
          }
          user_commands(ch);
        } while (ch == ERR);
      } else if(c->defeated == 0){
        npc_t* npc = dynamic_cast<npc_t*>(c);
        
        move_func[npc->mtype](c, d);
        world.cur_map->cmap[c->pos[dim_y]][c->pos[dim_x]] = NULL;
        world.cur_map->cmap[d[dim_y]][d[dim_x]] = c;
        c->next_turn += move_cost[npc->ctype][world.cur_map->map[d[dim_y]][d[dim_x]]];
        c->pos[dim_y] = d[dim_y];
        c->pos[dim_x] = d[dim_x];
        pathfind(world.cur_map);

        size_t npcPokeCount = npc->pokemon.size();

        if(npcPokeCount != 6 && world.cur_map->map[npc->pos[dim_y]][npc->pos[dim_x]] == ter_grass){
          int encounterChance = rng.generateInt(0, 99);

          if(encounterChance < 60){
            pokemonBattle(npc);
          }
        }
      }
      else{
        move_defeated_char(c);
      }
      heap_insert(&world.cur_map->turn, c);
      clear();  
      print_map();

    } else {
      break;
    }
    refresh();
    
  }

  endwin();
}

bool load_database(const std::string& path){
  
  std::ifstream file(path);

  if (!file.is_open()) {
      std::cerr << "Failed to open file: " << path << std::endl;
      return false; 
  }

  std::cout << "File opened successfully." << path << std::endl;

  return true;
}

  std::vector<std::string> parse_line(std::string line){
  std::istringstream stream(line);
  std::string data_cell;
  std::vector<std::string> data;

    while (std::getline(stream, data_cell, ',')) {
        data.push_back(data_cell);
    }

    return data;
}

int stoiVar(const std::string& str) {
    if (str.empty()) {
        return INT_MAX;
    } else {
        try {
            return std::stoi(str);
        } catch (const std::invalid_argument& err) {
            return INT_MAX;
        } catch (const std::out_of_range& err) {
            return INT_MAX;
        }
    }
}


void parse_pokeFile(const std::string& path, const std::string& fileName){
  std::ifstream file(path + "/pokedex/data/csv/"+ fileName);

  if (!file.is_open()) {
      std::cerr << "Failed to open file: " << path << std::endl;
  }

  std::string line;
  std::vector<std::string> v;

  std::getline(file, line);
  //std::cout << line << std::endl;

  if(fileName == "pokemon.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Pokemon p(stoiVar(v[0]), v[1], stoiVar(v[2]), stoiVar(v[3]), stoiVar(v[4]), stoiVar(v[5]), stoiVar(v[6]), stoiVar(v[7]));
    globalPokemon.push_back(p);

    //std::cout << p << std::endl;
  }
  }
  if(fileName == "moves.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Move m(stoiVar(v[0]), v[1], stoiVar(v[2]), stoiVar(v[3]), stoiVar(v[4]), stoiVar(v[5]), stoiVar(v[6]), 
    stoiVar(v[7]), stoiVar(v[8]), stoiVar(v[9]), stoiVar(v[10]), stoiVar(v[11]), stoiVar(v[12]), stoiVar(v[13]), stoiVar(v[14]));
    globalMoves.push_back(m);
    //std::cout << line << std::endl;
  }
  }
  if(fileName == "pokemon_moves.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Pokemon_Move pm(stoiVar(v[0]), stoiVar(v[1]), stoiVar(v[2]), stoiVar(v[3]), stoiVar(v[4]), stoiVar(v[5]));
    globalPokemonMoves.push_back(pm);
  }
  }
  if(fileName == "pokemon_species.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Pokemon_Species ps(stoiVar(v[0]), v[1], stoiVar(v[2]), stoiVar(v[3]), stoiVar(v[4]), stoiVar(v[5]), stoiVar(v[6]), 
    stoiVar(v[7]), stoiVar(v[8]), stoiVar(v[9]), stoiVar(v[10]), stoiVar(v[11]), stoiVar(v[12]), stoiVar(v[13]), 
    stoiVar(v[14]), stoiVar(v[15]), stoiVar(v[16]), stoiVar(v[17]), stoiVar(v[18]), stoiVar(v[19]));
    globalPokemonSpecies.push_back(ps);
    std::cout << ps.id << std::endl;
  }
  }
  if(fileName == "experience.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Experience e(stoiVar(v[0]), stoiVar(v[1]), stoiVar(v[2]));
    globalExperience.push_back(e);
    //std::cout << line << std::endl;
  }
  }
  if(fileName == "type_names.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    if(stoiVar(v[1]) == 9){
      Type_Name tn(stoiVar(v[0]), stoiVar(v[1]), v[2]);
      globalTypeNames.push_back(tn);
      //std::cout << line << std::endl;
    }
  }
  }
  if(fileName == "pokemon_stats.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Pokemon_Stats ps(stoiVar(v[0]), stoiVar(v[1]), stoiVar(v[2]), stoiVar(v[3]));
    globalPokemonStats.push_back(ps);
    //std::cout << line << std::endl;
  }
  }
  if(fileName == "stats.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Stats s(stoiVar(v[0]), stoiVar(v[1]), v[2], stoiVar(v[3]), stoiVar(v[4]));
    globalStats.push_back(s);
    //std::cout << line << std::endl;
  }
  }
  if(fileName == "pokemon_types.csv"){
    while(std::getline(file, line)){
    v = parse_line(line);
    Pokemon_Type pt(stoiVar(v[0]), stoiVar(v[1]), stoiVar(v[2]));
    globalPokemonTypes.push_back(pt);
    //std::cout << line << std::endl;
  }
  }
  
}




int main(int argc, char *argv[])
{
  class timeval tv;
  uint32_t seed;

  std::vector<std::string> paths = {
        std::string(strcat(getenv("HOME"), "/.poke327/pokedex/")),
        "/share/cs327/pokedex/",
        "'Pre-2024 Data'/ahisel/COMS327/pokedex/"
  };

  std::string path; 

  if (argc == 2) {
    seed = atoi(argv[1]);
  } else {
    gettimeofday(&tv, NULL);
    seed = (tv.tv_usec ^ (tv.tv_sec << 20)) & 0xffffffff;
  }

  srand(seed);


  for(int i = 0; i < 3; i++){
    if(load_database(paths[i])){
      path = paths[i];
      break;
    }
  }




  for (int i = 1; i < argc; i++) {
      if (strcmp(argv[i], "--numtrainers") == 0) {
          if (i + 1 < argc) {
              num_trainers = atoi(argv[i + 1]);
              i++;
          } else {
              printf("Error: --numtrainers requires an argument.\n");
              return 1;
          }
      }
      // else if (strcmp(argv[i], "pokemon") == 0){
      //   parse_pokeFile(path, "pokemon.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "moves") == 0){
      //   parse_pokeFile(path, "moves.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "pokemon_moves") == 0){
      //   parse_pokeFile(path, "pokemon_moves.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "pokemon_species") == 0){
      //   parse_pokeFile(path, "pokemon_species.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "experience") == 0){
      //   parse_pokeFile(path, "experience.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "type_names") == 0){
      //   parse_pokeFile(path, "type_names.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "pokemon_stats") == 0){
      //   parse_pokeFile(path, "pokemon_stats.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "stats") == 0){
      //   parse_pokeFile(path, "stats.csv");
      //   return 0;
      // }
      // else if(strcmp(argv[i], "pokemon_types") == 0){
      //   parse_pokeFile(path, "pokemon_types.csv");
      //   return 0;
      // }
  }

  parse_pokeFile(path, "pokemon.csv");
  parse_pokeFile(path, "moves.csv");
  parse_pokeFile(path, "pokemon_moves.csv");
  // parse_pokeFile(path, "pokemon_species.csv");
  // parse_pokeFile(path, "experience.csv");
  parse_pokeFile(path, "type_names.csv");
  parse_pokeFile(path, "pokemon_stats.csv");
  // parse_pokeFile(path, "stats.csv");
  parse_pokeFile(path, "pokemon_types.csv");

  if (num_trainers < 3){
    num_trainers = 3;
  }


  init_world();


  game_loop();
  delete_world();

  
  return 0;
}
