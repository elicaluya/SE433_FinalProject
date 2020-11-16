package shop.main;

import shop.ui.UI;
import shop.ui.UIMenuAction;
import shop.ui.UIError;
import shop.ui.UIFormTest;
import shop.ui.UIFormMenuBuilderFactory;
import shop.ui.UIFormMenu;

import shop.data.Data;
import shop.data.Inventory;
import shop.data.Video;
import shop.data.Record;
import shop.command.Command;
import java.util.Iterator;

class Control {
  private static final int EXITED = 0;
  private static final int EXIT = 1;
  private static final int START = 2;
  private static final int NUMSTATES = 10;
  private UIFormMenu[] _menus;
  private State _state;

  private UIFormMenu _getVideoForm;
  private UIFormTest _yearTest;
  private UIFormTest _numberTest;
  private UIFormTest _stringTest;
    
  private Inventory _inventory;
  private UI _ui;
  
  enum State {
	  StartState,
	  ExitState,
	  ExitedState
  }
  
  Control(Inventory inventory, UI ui) {
    _inventory = inventory;
    _ui = ui;

    _menus = new UIFormMenu[NUMSTATES];
    _state = State.StartState;
    addSTART(START);
    addEXIT(EXIT);
    
    _yearTest = new UIFormTest() {
        public boolean run(String input) {
          try {
            int i = Integer.parseInt(input);
            return i > 1800 && i < 5000;
          } catch (NumberFormatException e) {
            return false;
          }
        }
    };

    _numberTest = new UIFormTest() {
        public boolean run(String input) {
          try {
            Integer.parseInt(input);
            return true;
          } catch (NumberFormatException e) {
            return false;
          }
        }
    };

    _stringTest = new UIFormTest() {
		public boolean run(String input) {
		  return ! "".equals(input.trim());
		}
    };

    UIFormMenuBuilderFactory f = new UIFormMenuBuilderFactory();
    f.add("Title", _stringTest);
    f.add("Year", _yearTest);
    f.add("Director", _stringTest);
    _getVideoForm = f.toUIFormMenu("Enter Video");
  }
  
  void run() {
    try {
      while (_state != State.ExitedState) {
	  switch(_state) {
		case StartState:
			_ui.processMenu(_menus[START]);
			break;
		case ExitState:
			_ui.processMenu(_menus[EXIT]);
			break;
	  }
      }
    } catch (UIError e) {
      _ui.displayError("UI closed");
    }
  }

  private UIMenuAction defaultAction() {
	return new UIMenuAction() {
		public void run() {
			_ui.displayError("doh!");
		}
	};
  }

  private UIMenuAction addAction() {
	return new UIMenuAction() {
		public void run() {
			String[] result1 = _ui.processForm(_getVideoForm);
			Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

			UIFormMenuBuilderFactory f = new UIFormMenuBuilderFactory();
			f.add("Number of copies to add/remove", _numberTest);
			String[] result2 = _ui.processForm(f.toUIFormMenu(""));

			Command c = Data.newAddCmd(_inventory, v, Integer.parseInt(result2[0]));
			if (! c.run()) {
				_ui.displayError("Command failed");
			}
		}
	};
  }

  private UIMenuAction checkInAction() {
	return new UIMenuAction() {
		public void run() {
			String[] result1 = _ui.processForm(_getVideoForm);
			Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

			Command c = Data.newInCmd(_inventory, v);
			if (! c.run()) {
				_ui.displayError("Command failed");
			}
		}
	};
  }

  private UIMenuAction checkOutAction() {
	return new UIMenuAction() {
		public void run() {
			String[] result1 = _ui.processForm(_getVideoForm);
			Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

			Command c = Data.newOutCmd(_inventory, v);
			if (! c.run()) {
				_ui.displayError("Command failed");
			}
		}
	};
  }

  private UIMenuAction printAction() {
	return new UIMenuAction() {
		public void run() {
			_ui.displayMessage(_inventory.toString());
		}
	};
  }

  private UIMenuAction clearAction() {
	return new UIMenuAction() {
		public void run() {
			if (!Data.newClearCmd(_inventory).run()) {
				_ui.displayError("Command failed");
			}
		}
	};
  }

  private UIMenuAction undoAction() {
	return new UIMenuAction() {
		public void run() {
			if (!Data.newUndoCmd(_inventory).run()) {
				_ui.displayError("Command failed");
			}
		}
	};
  }

  private UIMenuAction redoAction() {
	return new UIMenuAction() {
		public void run() {
			if (!Data.newRedoCmd(_inventory).run()) {
				_ui.displayError("Command failed");
			}
		}
	};
  }

  private UIMenuAction topAction() {
	return new UIMenuAction() {
		public void run() {
			Iterator<Record> itr = _inventory.iterator((r1,r2) -> r2.numRentals() - r1.numRentals());
			int count = 0;
			StringBuilder s = new StringBuilder();
			while (itr.hasNext() && count < 10) {
				Record r = itr.next();
				s.append(r.toString()+ "\n");
				count++;
			}
			_ui.displayMessage(s.toString());
		}
	};
  }

  private UIMenuAction exitEntryAction() {
	return new UIMenuAction() {
		public void run() {
			_state = State.ExitState;
		}
	};
  }

  private UIMenuAction initAction() {
	return new UIMenuAction() {
		public void run() {
			char letter = 'a';
			for (int i = 0; i < 26; i++) {
				Data.newAddCmd(_inventory, Data.newVideo(String.valueOf(letter), 2000, "m"), i + 1).run();
				letter++;
			}
		}
	};
  }

  private UIMenuAction yesAction() {
	return new UIMenuAction() {
		public void run() {
			_state = State.ExitedState;
		}
	};
  }

  private UIMenuAction noAction() {
	return new UIMenuAction() {
		public void run() {
			_state = State.StartState;
		}
	};
  }
  
  private void addSTART(int stateNum) {
	  UIFormMenuBuilderFactory m = new UIFormMenuBuilderFactory();
	  m.add("Default", defaultAction());
	  m.add("Add/Remove copies of a video", addAction());
	  m.add("Check in a video", checkInAction());
	  m.add("Check out a video", checkOutAction());
	  m.add("Print the inventory", printAction());
	  m.add("Clear the inventory", clearAction());
	  m.add("Undo", undoAction());
	  m.add("Redo", redoAction());
	  m.add("Print top ten all time rentals in order", topAction());
	  m.add("Exit", exitEntryAction());
	  m.add("Initialize with bogus contents", initAction());
	  _menus[stateNum] = m.toUIFormMenu("Bob's Video");
  }
  
  private void addEXIT(int stateNum) {
	  UIFormMenuBuilderFactory m = new UIFormMenuBuilderFactory();
	  m.add("Default", defaultAction());
	  m.add("Yes", yesAction());
	  m.add("No", noAction());
	  _menus[stateNum] = m.toUIFormMenu("Are you sure you want to exit?");
  }
}
