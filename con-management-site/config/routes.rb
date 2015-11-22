Rails.application.routes.draw do

  # site root
  root 'home#home'

  # home controller ============================================================
  get 'home' => 'home#home'
  # ============================================================================

  # user controller ============================================================
  # new accounts
  get 'signup' => 'user#new'
  post 'signup' => 'user#create'

  # session management
  get 'login' => 'user#login_page'
  post 'login' => 'user#login'
  post 'logout' => 'user#logout'

  # user's conventions
  get 'conventions/mine' => 'user#conventions'
  # ============================================================================

  # convention controller ======================================================
  # actions not for a specific convention
  get 'conventions/all' => 'convention#all'
  get 'convention/search'
  get 'convention/new'
  post 'convention/new' => 'convention#create_convention'

  # actions for a specific convention
  get 'convention/:con_name' => 'convention#index'
  get 'convention/:con_name/index' => 'convention#index'
  patch 'convention/:con_name/delete' => 'convention#delete'
  get 'convention/:con_name/schedule' => 'convention#schedule'

  #details
  get 'convention/:con_name/details' => 'convention#details'
  get 'convention/:con_name/edit' => 'convention#edit'
  post 'convention/:con_name/edit' => 'convention#edit_details'

  # organizers
  get 'convention/:con_name/organizers' => 'convention#organizers'
  post 'convention/:con_name/add_organizer' => 'convention#add_organizer'
  post 'conventions/:con_name/organizers/change_role' => 'convention#change_organizer_role'
  patch 'convention/:con_name/remove_organizer' => 'convention#remove_organizer'

  # rooms
  post 'convention/:con_name/rooms/add' => 'convention#add_room'
  patch 'convention/:con_name/remove_room/:room_name' => 'convention#remove_room'

  # hosts
  post 'convention/:con_name/hosts/add' => 'convention#add_host'
  patch 'convention/:con_name/remove_host/:host_name' => 'convention#remove_host'

  #documents
  get 'convention/:con_name/documents' => 'convention#documents'
  post 'convention/:con_name/documents/add' => 'convention#upload_document'
  patch 'convention/:con_name/remove_document/:doc_name' => 'convention#remove_document'

  # mobile application
  get 'convention/client_search'
  get 'convention/:convention_name/download' => 'convention#download'
  # ============================================================================

  # event controller ===========================================================
  get 'convention/:con_name/events' => 'event#events'
  get 'convention/:con_name/events/add' => 'event#add'
  post 'convention/:con_name/events/add' => 'event#create'
  get 'convention/:con_name/events/:event_name/edit' => 'event#edit'
  post 'convention/:con_name/events/:event_name/edit' => 'event#edit_details'
  patch 'convention/:con_name/remove_event/:event_name' => 'event#remove'
  # ============================================================================

  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # Example of named route that can be invoked with purchase_url(id: product.id)
  #   get 'products/:id/purchase' => 'catalog#purchase', as: :purchase

  # Example resource route (maps HTTP verbs to controller actions automatically):
  #   resources :products

  # Example resource route with options:
  #   resources :products do
  #     member do
  #       get 'short'
  #       post 'toggle'
  #     end
  #
  #     collection do
  #       get 'sold'
  #     end
  #   end

  # Example resource route with sub-resources:
  #   resources :products do
  #     resources :comments, :sales
  #     resource :seller
  #   end

  # Example resource route with more complex sub-resources:
  #   resources :products do
  #     resources :comments
  #     resources :sales do
  #       get 'recent', on: :collection
  #     end
  #   end

  # Example resource route with concerns:
  #   concern :toggleable do
  #     post 'toggle'
  #   end
  #   resources :posts, concerns: :toggleable
  #   resources :photos, concerns: :toggleable

  # Example resource route within a namespace:
  #   namespace :admin do
  #     # Directs /admin/products/* to Admin::ProductsController
  #     # (app/controllers/admin/products_controller.rb)
  #     resources :products
  #   end
end
