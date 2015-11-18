Rails.application.routes.draw do

  # start at 'home/index' page
  root 'home#index'

  # home controller pages
  get 'home/index'

  get 'signup' => 'user#new'
  post 'signup' => 'user#create'
  get 'login' => 'user#login'
  post 'login' => 'user#login_x'

  # convention controller pages
  get 'convention/new'
  get 'convention/all'
  get 'convention/:con_name/index' => 'convention#index'
  get 'convention/:con_name/edit' => 'convention#edit'
  get 'convention/:con_name/details' => 'convention#details'
  get 'convention/:con_name/schedule' => 'convention#schedule'
  get 'convention/:con_name/documents' => 'convention#documents'

  # convention controller creations
  post 'convention/new' => 'convention#create_convention'
  post 'convention/:con_name/rooms/add' => 'convention#add_room'
  post 'convention/:con_name/hosts/add' => 'convention#add_host'
  post 'convention/:con_name/documents/add' => 'convention#upload_document'
  # convention controller deletions
  post 'convention/:con_name/edit' => 'convention#edit_details'
  patch 'convention/:con_name/delete' => 'convention#delete'
  patch 'convention/:con_name/remove_room/:room_name' => 'convention#remove_room'
  patch 'convention/:con_name/remove_host/:host_name' => 'convention#remove_host'
  patch 'convention/:con_name/remove_document/:doc_name' => 'convention#remove_document'

  # event controller
  get 'convention/:con_name/events' => 'event#events'
  get 'convention/:con_name/events/add' => 'event#add'
  post 'convention/:con_name/events/add' => 'event#create'
  get 'convention/:con_name/events/:event_name/edit' => 'event#edit'
  post 'convention/:con_name/events/:event_name/edit' => 'event#edit_details'
  patch 'convention/:con_name/remove_event/:event_name' => 'event#remove'

  # The priority is based upon order of creation: first created -> highest priority.
  # See how all your routes lay out with "rake routes".

  # You can have the root of your site routed with "root"
  # root 'welcome#index'

  # Example of regular route:
  #   get 'products/:id' => 'catalog#view'

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
